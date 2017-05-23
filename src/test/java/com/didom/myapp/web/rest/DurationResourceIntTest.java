package com.didom.myapp.web.rest;

import com.didom.myapp.DiDomApp;

import com.didom.myapp.domain.Duration;
import com.didom.myapp.repository.DurationRepository;
import com.didom.myapp.repository.search.DurationSearchRepository;
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
 * Test class for the DurationResource REST controller.
 *
 * @see DurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiDomApp.class)
public class DurationResourceIntTest {

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    @Autowired
    private DurationRepository durationRepository;

    @Autowired
    private DurationSearchRepository durationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDurationMockMvc;

    private Duration duration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DurationResource durationResource = new DurationResource(durationRepository, durationSearchRepository);
        this.restDurationMockMvc = MockMvcBuilders.standaloneSetup(durationResource)
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
    public static Duration createEntity(EntityManager em) {
        Duration duration = new Duration()
            .duration(DEFAULT_DURATION);
        return duration;
    }

    @Before
    public void initTest() {
        durationSearchRepository.deleteAll();
        duration = createEntity(em);
    }

    @Test
    @Transactional
    public void createDuration() throws Exception {
        int databaseSizeBeforeCreate = durationRepository.findAll().size();

        // Create the Duration
        restDurationMockMvc.perform(post("/api/durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duration)))
            .andExpect(status().isCreated());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeCreate + 1);
        Duration testDuration = durationList.get(durationList.size() - 1);
        assertThat(testDuration.getDuration()).isEqualTo(DEFAULT_DURATION);

        // Validate the Duration in Elasticsearch
        Duration durationEs = durationSearchRepository.findOne(testDuration.getId());
        assertThat(durationEs).isEqualToComparingFieldByField(testDuration);
    }

    @Test
    @Transactional
    public void createDurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = durationRepository.findAll().size();

        // Create the Duration with an existing ID
        duration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDurationMockMvc.perform(post("/api/durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duration)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = durationRepository.findAll().size();
        // set the field null
        duration.setDuration(null);

        // Create the Duration, which fails.

        restDurationMockMvc.perform(post("/api/durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duration)))
            .andExpect(status().isBadRequest());

        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDurations() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        // Get all the durationList
        restDurationMockMvc.perform(get("/api/durations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duration.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    public void getDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);

        // Get the duration
        restDurationMockMvc.perform(get("/api/durations/{id}", duration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(duration.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDuration() throws Exception {
        // Get the duration
        restDurationMockMvc.perform(get("/api/durations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);
        durationSearchRepository.save(duration);
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();

        // Update the duration
        Duration updatedDuration = durationRepository.findOne(duration.getId());
        updatedDuration
            .duration(UPDATED_DURATION);

        restDurationMockMvc.perform(put("/api/durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDuration)))
            .andExpect(status().isOk());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate);
        Duration testDuration = durationList.get(durationList.size() - 1);
        assertThat(testDuration.getDuration()).isEqualTo(UPDATED_DURATION);

        // Validate the Duration in Elasticsearch
        Duration durationEs = durationSearchRepository.findOne(testDuration.getId());
        assertThat(durationEs).isEqualToComparingFieldByField(testDuration);
    }

    @Test
    @Transactional
    public void updateNonExistingDuration() throws Exception {
        int databaseSizeBeforeUpdate = durationRepository.findAll().size();

        // Create the Duration

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDurationMockMvc.perform(put("/api/durations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(duration)))
            .andExpect(status().isCreated());

        // Validate the Duration in the database
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);
        durationSearchRepository.save(duration);
        int databaseSizeBeforeDelete = durationRepository.findAll().size();

        // Get the duration
        restDurationMockMvc.perform(delete("/api/durations/{id}", duration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean durationExistsInEs = durationSearchRepository.exists(duration.getId());
        assertThat(durationExistsInEs).isFalse();

        // Validate the database is empty
        List<Duration> durationList = durationRepository.findAll();
        assertThat(durationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDuration() throws Exception {
        // Initialize the database
        durationRepository.saveAndFlush(duration);
        durationSearchRepository.save(duration);

        // Search the duration
        restDurationMockMvc.perform(get("/api/_search/durations?query=id:" + duration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(duration.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Duration.class);
        Duration duration1 = new Duration();
        duration1.setId(1L);
        Duration duration2 = new Duration();
        duration2.setId(duration1.getId());
        assertThat(duration1).isEqualTo(duration2);
        duration2.setId(2L);
        assertThat(duration1).isNotEqualTo(duration2);
        duration1.setId(null);
        assertThat(duration1).isNotEqualTo(duration2);
    }
}
