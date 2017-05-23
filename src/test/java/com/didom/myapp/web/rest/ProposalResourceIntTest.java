package com.didom.myapp.web.rest;

import com.didom.myapp.DiDomApp;

import com.didom.myapp.domain.Proposal;
import com.didom.myapp.repository.ProposalRepository;
import com.didom.myapp.service.ProposalService;
import com.didom.myapp.repository.search.ProposalSearchRepository;
import com.didom.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.didom.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProposalResource REST controller.
 *
 * @see ProposalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiDomApp.class)
public class ProposalResourceIntTest {

    private static final ZonedDateTime DEFAULT_PROPOSAL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PROPOSAL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_CLIENT_GRADE = 1;
    private static final Integer UPDATED_CLIENT_GRADE = 2;

    private static final String DEFAULT_CLIENT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_COMMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_FREELANCE_GRADE = 1;
    private static final Integer UPDATED_FREELANCE_GRADE = 2;

    private static final String DEFAULT_FREELANCE_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_FREELANCE_COMMENT = "BBBBBBBBBB";

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private ProposalSearchRepository proposalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProposalMockMvc;

    private Proposal proposal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProposalResource proposalResource = new ProposalResource(proposalService);
        this.restProposalMockMvc = MockMvcBuilders.standaloneSetup(proposalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposal createEntity(EntityManager em) {
        Proposal proposal = new Proposal()
            .proposalTime(DEFAULT_PROPOSAL_TIME)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .clientGrade(DEFAULT_CLIENT_GRADE)
            .clientComment(DEFAULT_CLIENT_COMMENT)
            .freelanceGrade(DEFAULT_FREELANCE_GRADE)
            .freelanceComment(DEFAULT_FREELANCE_COMMENT);
        return proposal;
    }

    @Before
    public void initTest() {
        proposalSearchRepository.deleteAll();
        proposal = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposal() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // Create the Proposal
        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate + 1);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getProposalTime()).isEqualTo(DEFAULT_PROPOSAL_TIME);
        assertThat(testProposal.getPaymentAmount()).isEqualTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testProposal.getClientGrade()).isEqualTo(DEFAULT_CLIENT_GRADE);
        assertThat(testProposal.getClientComment()).isEqualTo(DEFAULT_CLIENT_COMMENT);
        assertThat(testProposal.getFreelanceGrade()).isEqualTo(DEFAULT_FREELANCE_GRADE);
        assertThat(testProposal.getFreelanceComment()).isEqualTo(DEFAULT_FREELANCE_COMMENT);

        // Validate the Proposal in Elasticsearch
        Proposal proposalEs = proposalSearchRepository.findOne(testProposal.getId());
        assertThat(proposalEs).isEqualToComparingFieldByField(testProposal);
    }

    @Test
    @Transactional
    public void createProposalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proposalRepository.findAll().size();

        // Create the Proposal with an existing ID
        proposal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProposalTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setProposalTime(null);

        // Create the Proposal, which fails.

        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalRepository.findAll().size();
        // set the field null
        proposal.setPaymentAmount(null);

        // Create the Proposal, which fails.

        restProposalMockMvc.perform(post("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isBadRequest());

        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProposals() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get all the proposalList
        restProposalMockMvc.perform(get("/api/proposals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].proposalTime").value(hasItem(sameInstant(DEFAULT_PROPOSAL_TIME))))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].clientGrade").value(hasItem(DEFAULT_CLIENT_GRADE)))
            .andExpect(jsonPath("$.[*].clientComment").value(hasItem(DEFAULT_CLIENT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].freelanceGrade").value(hasItem(DEFAULT_FREELANCE_GRADE)))
            .andExpect(jsonPath("$.[*].freelanceComment").value(hasItem(DEFAULT_FREELANCE_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getProposal() throws Exception {
        // Initialize the database
        proposalRepository.saveAndFlush(proposal);

        // Get the proposal
        restProposalMockMvc.perform(get("/api/proposals/{id}", proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proposal.getId().intValue()))
            .andExpect(jsonPath("$.proposalTime").value(sameInstant(DEFAULT_PROPOSAL_TIME)))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.clientGrade").value(DEFAULT_CLIENT_GRADE))
            .andExpect(jsonPath("$.clientComment").value(DEFAULT_CLIENT_COMMENT.toString()))
            .andExpect(jsonPath("$.freelanceGrade").value(DEFAULT_FREELANCE_GRADE))
            .andExpect(jsonPath("$.freelanceComment").value(DEFAULT_FREELANCE_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProposal() throws Exception {
        // Get the proposal
        restProposalMockMvc.perform(get("/api/proposals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposal() throws Exception {
        // Initialize the database
        proposalService.save(proposal);

        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Update the proposal
        Proposal updatedProposal = proposalRepository.findOne(proposal.getId());
        updatedProposal
            .proposalTime(UPDATED_PROPOSAL_TIME)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .clientGrade(UPDATED_CLIENT_GRADE)
            .clientComment(UPDATED_CLIENT_COMMENT)
            .freelanceGrade(UPDATED_FREELANCE_GRADE)
            .freelanceComment(UPDATED_FREELANCE_COMMENT);

        restProposalMockMvc.perform(put("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProposal)))
            .andExpect(status().isOk());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate);
        Proposal testProposal = proposalList.get(proposalList.size() - 1);
        assertThat(testProposal.getProposalTime()).isEqualTo(UPDATED_PROPOSAL_TIME);
        assertThat(testProposal.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testProposal.getClientGrade()).isEqualTo(UPDATED_CLIENT_GRADE);
        assertThat(testProposal.getClientComment()).isEqualTo(UPDATED_CLIENT_COMMENT);
        assertThat(testProposal.getFreelanceGrade()).isEqualTo(UPDATED_FREELANCE_GRADE);
        assertThat(testProposal.getFreelanceComment()).isEqualTo(UPDATED_FREELANCE_COMMENT);

        // Validate the Proposal in Elasticsearch
        Proposal proposalEs = proposalSearchRepository.findOne(testProposal.getId());
        assertThat(proposalEs).isEqualToComparingFieldByField(testProposal);
    }

    @Test
    @Transactional
    public void updateNonExistingProposal() throws Exception {
        int databaseSizeBeforeUpdate = proposalRepository.findAll().size();

        // Create the Proposal

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProposalMockMvc.perform(put("/api/proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposal)))
            .andExpect(status().isCreated());

        // Validate the Proposal in the database
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProposal() throws Exception {
        // Initialize the database
        proposalService.save(proposal);

        int databaseSizeBeforeDelete = proposalRepository.findAll().size();

        // Get the proposal
        restProposalMockMvc.perform(delete("/api/proposals/{id}", proposal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean proposalExistsInEs = proposalSearchRepository.exists(proposal.getId());
        assertThat(proposalExistsInEs).isFalse();

        // Validate the database is empty
        List<Proposal> proposalList = proposalRepository.findAll();
        assertThat(proposalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProposal() throws Exception {
        // Initialize the database
        proposalService.save(proposal);

        // Search the proposal
        restProposalMockMvc.perform(get("/api/_search/proposals?query=id:" + proposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].proposalTime").value(hasItem(sameInstant(DEFAULT_PROPOSAL_TIME))))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].clientGrade").value(hasItem(DEFAULT_CLIENT_GRADE)))
            .andExpect(jsonPath("$.[*].clientComment").value(hasItem(DEFAULT_CLIENT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].freelanceGrade").value(hasItem(DEFAULT_FREELANCE_GRADE)))
            .andExpect(jsonPath("$.[*].freelanceComment").value(hasItem(DEFAULT_FREELANCE_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proposal.class);
        Proposal proposal1 = new Proposal();
        proposal1.setId(1L);
        Proposal proposal2 = new Proposal();
        proposal2.setId(proposal1.getId());
        assertThat(proposal1).isEqualTo(proposal2);
        proposal2.setId(2L);
        assertThat(proposal1).isNotEqualTo(proposal2);
        proposal1.setId(null);
        assertThat(proposal1).isNotEqualTo(proposal2);
    }
}
