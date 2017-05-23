package com.didom.myapp.web.rest;

import com.didom.myapp.DiDomApp;

import com.didom.myapp.domain.ProposalStatusCatalog;
import com.didom.myapp.repository.ProposalStatusCatalogRepository;
import com.didom.myapp.repository.search.ProposalStatusCatalogSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProposalStatusCatalogResource REST controller.
 *
 * @see ProposalStatusCatalogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiDomApp.class)
public class ProposalStatusCatalogResourceIntTest {

    private static final String DEFAULT_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_NAME = "BBBBBBBBBB";

    @Autowired
    private ProposalStatusCatalogRepository proposalStatusCatalogRepository;

    @Autowired
    private ProposalStatusCatalogSearchRepository proposalStatusCatalogSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProposalStatusCatalogMockMvc;

    private ProposalStatusCatalog proposalStatusCatalog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProposalStatusCatalogResource proposalStatusCatalogResource = new ProposalStatusCatalogResource(proposalStatusCatalogRepository, proposalStatusCatalogSearchRepository);
        this.restProposalStatusCatalogMockMvc = MockMvcBuilders.standaloneSetup(proposalStatusCatalogResource)
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
    public static ProposalStatusCatalog createEntity(EntityManager em) {
        ProposalStatusCatalog proposalStatusCatalog = new ProposalStatusCatalog()
            .statusName(DEFAULT_STATUS_NAME);
        return proposalStatusCatalog;
    }

    @Before
    public void initTest() {
        proposalStatusCatalogSearchRepository.deleteAll();
        proposalStatusCatalog = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposalStatusCatalog() throws Exception {
        int databaseSizeBeforeCreate = proposalStatusCatalogRepository.findAll().size();

        // Create the ProposalStatusCatalog
        restProposalStatusCatalogMockMvc.perform(post("/api/proposal-status-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalStatusCatalog)))
            .andExpect(status().isCreated());

        // Validate the ProposalStatusCatalog in the database
        List<ProposalStatusCatalog> proposalStatusCatalogList = proposalStatusCatalogRepository.findAll();
        assertThat(proposalStatusCatalogList).hasSize(databaseSizeBeforeCreate + 1);
        ProposalStatusCatalog testProposalStatusCatalog = proposalStatusCatalogList.get(proposalStatusCatalogList.size() - 1);
        assertThat(testProposalStatusCatalog.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);

        // Validate the ProposalStatusCatalog in Elasticsearch
        ProposalStatusCatalog proposalStatusCatalogEs = proposalStatusCatalogSearchRepository.findOne(testProposalStatusCatalog.getId());
        assertThat(proposalStatusCatalogEs).isEqualToComparingFieldByField(testProposalStatusCatalog);
    }

    @Test
    @Transactional
    public void createProposalStatusCatalogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proposalStatusCatalogRepository.findAll().size();

        // Create the ProposalStatusCatalog with an existing ID
        proposalStatusCatalog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalStatusCatalogMockMvc.perform(post("/api/proposal-status-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalStatusCatalog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ProposalStatusCatalog> proposalStatusCatalogList = proposalStatusCatalogRepository.findAll();
        assertThat(proposalStatusCatalogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProposalStatusCatalogs() throws Exception {
        // Initialize the database
        proposalStatusCatalogRepository.saveAndFlush(proposalStatusCatalog);

        // Get all the proposalStatusCatalogList
        restProposalStatusCatalogMockMvc.perform(get("/api/proposal-status-catalogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalStatusCatalog.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProposalStatusCatalog() throws Exception {
        // Initialize the database
        proposalStatusCatalogRepository.saveAndFlush(proposalStatusCatalog);

        // Get the proposalStatusCatalog
        restProposalStatusCatalogMockMvc.perform(get("/api/proposal-status-catalogs/{id}", proposalStatusCatalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proposalStatusCatalog.getId().intValue()))
            .andExpect(jsonPath("$.statusName").value(DEFAULT_STATUS_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProposalStatusCatalog() throws Exception {
        // Get the proposalStatusCatalog
        restProposalStatusCatalogMockMvc.perform(get("/api/proposal-status-catalogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposalStatusCatalog() throws Exception {
        // Initialize the database
        proposalStatusCatalogRepository.saveAndFlush(proposalStatusCatalog);
        proposalStatusCatalogSearchRepository.save(proposalStatusCatalog);
        int databaseSizeBeforeUpdate = proposalStatusCatalogRepository.findAll().size();

        // Update the proposalStatusCatalog
        ProposalStatusCatalog updatedProposalStatusCatalog = proposalStatusCatalogRepository.findOne(proposalStatusCatalog.getId());
        updatedProposalStatusCatalog
            .statusName(UPDATED_STATUS_NAME);

        restProposalStatusCatalogMockMvc.perform(put("/api/proposal-status-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProposalStatusCatalog)))
            .andExpect(status().isOk());

        // Validate the ProposalStatusCatalog in the database
        List<ProposalStatusCatalog> proposalStatusCatalogList = proposalStatusCatalogRepository.findAll();
        assertThat(proposalStatusCatalogList).hasSize(databaseSizeBeforeUpdate);
        ProposalStatusCatalog testProposalStatusCatalog = proposalStatusCatalogList.get(proposalStatusCatalogList.size() - 1);
        assertThat(testProposalStatusCatalog.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);

        // Validate the ProposalStatusCatalog in Elasticsearch
        ProposalStatusCatalog proposalStatusCatalogEs = proposalStatusCatalogSearchRepository.findOne(testProposalStatusCatalog.getId());
        assertThat(proposalStatusCatalogEs).isEqualToComparingFieldByField(testProposalStatusCatalog);
    }

    @Test
    @Transactional
    public void updateNonExistingProposalStatusCatalog() throws Exception {
        int databaseSizeBeforeUpdate = proposalStatusCatalogRepository.findAll().size();

        // Create the ProposalStatusCatalog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProposalStatusCatalogMockMvc.perform(put("/api/proposal-status-catalogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalStatusCatalog)))
            .andExpect(status().isCreated());

        // Validate the ProposalStatusCatalog in the database
        List<ProposalStatusCatalog> proposalStatusCatalogList = proposalStatusCatalogRepository.findAll();
        assertThat(proposalStatusCatalogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProposalStatusCatalog() throws Exception {
        // Initialize the database
        proposalStatusCatalogRepository.saveAndFlush(proposalStatusCatalog);
        proposalStatusCatalogSearchRepository.save(proposalStatusCatalog);
        int databaseSizeBeforeDelete = proposalStatusCatalogRepository.findAll().size();

        // Get the proposalStatusCatalog
        restProposalStatusCatalogMockMvc.perform(delete("/api/proposal-status-catalogs/{id}", proposalStatusCatalog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean proposalStatusCatalogExistsInEs = proposalStatusCatalogSearchRepository.exists(proposalStatusCatalog.getId());
        assertThat(proposalStatusCatalogExistsInEs).isFalse();

        // Validate the database is empty
        List<ProposalStatusCatalog> proposalStatusCatalogList = proposalStatusCatalogRepository.findAll();
        assertThat(proposalStatusCatalogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProposalStatusCatalog() throws Exception {
        // Initialize the database
        proposalStatusCatalogRepository.saveAndFlush(proposalStatusCatalog);
        proposalStatusCatalogSearchRepository.save(proposalStatusCatalog);

        // Search the proposalStatusCatalog
        restProposalStatusCatalogMockMvc.perform(get("/api/_search/proposal-status-catalogs?query=id:" + proposalStatusCatalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalStatusCatalog.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposalStatusCatalog.class);
        ProposalStatusCatalog proposalStatusCatalog1 = new ProposalStatusCatalog();
        proposalStatusCatalog1.setId(1L);
        ProposalStatusCatalog proposalStatusCatalog2 = new ProposalStatusCatalog();
        proposalStatusCatalog2.setId(proposalStatusCatalog1.getId());
        assertThat(proposalStatusCatalog1).isEqualTo(proposalStatusCatalog2);
        proposalStatusCatalog2.setId(2L);
        assertThat(proposalStatusCatalog1).isNotEqualTo(proposalStatusCatalog2);
        proposalStatusCatalog1.setId(null);
        assertThat(proposalStatusCatalog1).isNotEqualTo(proposalStatusCatalog2);
    }
}
