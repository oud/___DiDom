package com.didom.myapp.web.rest;

import com.didom.myapp.DiDomApp;

import com.didom.myapp.domain.HasSkill;
import com.didom.myapp.repository.HasSkillRepository;
import com.didom.myapp.repository.search.HasSkillSearchRepository;
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
 * Test class for the HasSkillResource REST controller.
 *
 * @see HasSkillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiDomApp.class)
public class HasSkillResourceIntTest {

    @Autowired
    private HasSkillRepository hasSkillRepository;

    @Autowired
    private HasSkillSearchRepository hasSkillSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHasSkillMockMvc;

    private HasSkill hasSkill;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HasSkillResource hasSkillResource = new HasSkillResource(hasSkillRepository, hasSkillSearchRepository);
        this.restHasSkillMockMvc = MockMvcBuilders.standaloneSetup(hasSkillResource)
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
    public static HasSkill createEntity(EntityManager em) {
        HasSkill hasSkill = new HasSkill();
        return hasSkill;
    }

    @Before
    public void initTest() {
        hasSkillSearchRepository.deleteAll();
        hasSkill = createEntity(em);
    }

    @Test
    @Transactional
    public void createHasSkill() throws Exception {
        int databaseSizeBeforeCreate = hasSkillRepository.findAll().size();

        // Create the HasSkill
        restHasSkillMockMvc.perform(post("/api/has-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hasSkill)))
            .andExpect(status().isCreated());

        // Validate the HasSkill in the database
        List<HasSkill> hasSkillList = hasSkillRepository.findAll();
        assertThat(hasSkillList).hasSize(databaseSizeBeforeCreate + 1);
        HasSkill testHasSkill = hasSkillList.get(hasSkillList.size() - 1);

        // Validate the HasSkill in Elasticsearch
        HasSkill hasSkillEs = hasSkillSearchRepository.findOne(testHasSkill.getId());
        assertThat(hasSkillEs).isEqualToComparingFieldByField(testHasSkill);
    }

    @Test
    @Transactional
    public void createHasSkillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hasSkillRepository.findAll().size();

        // Create the HasSkill with an existing ID
        hasSkill.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHasSkillMockMvc.perform(post("/api/has-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hasSkill)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HasSkill> hasSkillList = hasSkillRepository.findAll();
        assertThat(hasSkillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHasSkills() throws Exception {
        // Initialize the database
        hasSkillRepository.saveAndFlush(hasSkill);

        // Get all the hasSkillList
        restHasSkillMockMvc.perform(get("/api/has-skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hasSkill.getId().intValue())));
    }

    @Test
    @Transactional
    public void getHasSkill() throws Exception {
        // Initialize the database
        hasSkillRepository.saveAndFlush(hasSkill);

        // Get the hasSkill
        restHasSkillMockMvc.perform(get("/api/has-skills/{id}", hasSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hasSkill.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHasSkill() throws Exception {
        // Get the hasSkill
        restHasSkillMockMvc.perform(get("/api/has-skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHasSkill() throws Exception {
        // Initialize the database
        hasSkillRepository.saveAndFlush(hasSkill);
        hasSkillSearchRepository.save(hasSkill);
        int databaseSizeBeforeUpdate = hasSkillRepository.findAll().size();

        // Update the hasSkill
        HasSkill updatedHasSkill = hasSkillRepository.findOne(hasSkill.getId());

        restHasSkillMockMvc.perform(put("/api/has-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHasSkill)))
            .andExpect(status().isOk());

        // Validate the HasSkill in the database
        List<HasSkill> hasSkillList = hasSkillRepository.findAll();
        assertThat(hasSkillList).hasSize(databaseSizeBeforeUpdate);
        HasSkill testHasSkill = hasSkillList.get(hasSkillList.size() - 1);

        // Validate the HasSkill in Elasticsearch
        HasSkill hasSkillEs = hasSkillSearchRepository.findOne(testHasSkill.getId());
        assertThat(hasSkillEs).isEqualToComparingFieldByField(testHasSkill);
    }

    @Test
    @Transactional
    public void updateNonExistingHasSkill() throws Exception {
        int databaseSizeBeforeUpdate = hasSkillRepository.findAll().size();

        // Create the HasSkill

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHasSkillMockMvc.perform(put("/api/has-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hasSkill)))
            .andExpect(status().isCreated());

        // Validate the HasSkill in the database
        List<HasSkill> hasSkillList = hasSkillRepository.findAll();
        assertThat(hasSkillList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHasSkill() throws Exception {
        // Initialize the database
        hasSkillRepository.saveAndFlush(hasSkill);
        hasSkillSearchRepository.save(hasSkill);
        int databaseSizeBeforeDelete = hasSkillRepository.findAll().size();

        // Get the hasSkill
        restHasSkillMockMvc.perform(delete("/api/has-skills/{id}", hasSkill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean hasSkillExistsInEs = hasSkillSearchRepository.exists(hasSkill.getId());
        assertThat(hasSkillExistsInEs).isFalse();

        // Validate the database is empty
        List<HasSkill> hasSkillList = hasSkillRepository.findAll();
        assertThat(hasSkillList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHasSkill() throws Exception {
        // Initialize the database
        hasSkillRepository.saveAndFlush(hasSkill);
        hasSkillSearchRepository.save(hasSkill);

        // Search the hasSkill
        restHasSkillMockMvc.perform(get("/api/_search/has-skills?query=id:" + hasSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hasSkill.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HasSkill.class);
        HasSkill hasSkill1 = new HasSkill();
        hasSkill1.setId(1L);
        HasSkill hasSkill2 = new HasSkill();
        hasSkill2.setId(hasSkill1.getId());
        assertThat(hasSkill1).isEqualTo(hasSkill2);
        hasSkill2.setId(2L);
        assertThat(hasSkill1).isNotEqualTo(hasSkill2);
        hasSkill1.setId(null);
        assertThat(hasSkill1).isNotEqualTo(hasSkill2);
    }
}
