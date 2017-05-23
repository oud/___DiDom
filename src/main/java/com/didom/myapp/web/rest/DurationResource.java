package com.didom.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.didom.myapp.domain.Duration;

import com.didom.myapp.repository.DurationRepository;
import com.didom.myapp.repository.search.DurationSearchRepository;
import com.didom.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Duration.
 */
@RestController
@RequestMapping("/api")
public class DurationResource {

    private final Logger log = LoggerFactory.getLogger(DurationResource.class);

    private static final String ENTITY_NAME = "duration";
        
    private final DurationRepository durationRepository;

    private final DurationSearchRepository durationSearchRepository;

    public DurationResource(DurationRepository durationRepository, DurationSearchRepository durationSearchRepository) {
        this.durationRepository = durationRepository;
        this.durationSearchRepository = durationSearchRepository;
    }

    /**
     * POST  /durations : Create a new duration.
     *
     * @param duration the duration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new duration, or with status 400 (Bad Request) if the duration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/durations")
    @Timed
    public ResponseEntity<Duration> createDuration(@Valid @RequestBody Duration duration) throws URISyntaxException {
        log.debug("REST request to save Duration : {}", duration);
        if (duration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new duration cannot already have an ID")).body(null);
        }
        Duration result = durationRepository.save(duration);
        durationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/durations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /durations : Updates an existing duration.
     *
     * @param duration the duration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated duration,
     * or with status 400 (Bad Request) if the duration is not valid,
     * or with status 500 (Internal Server Error) if the duration couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/durations")
    @Timed
    public ResponseEntity<Duration> updateDuration(@Valid @RequestBody Duration duration) throws URISyntaxException {
        log.debug("REST request to update Duration : {}", duration);
        if (duration.getId() == null) {
            return createDuration(duration);
        }
        Duration result = durationRepository.save(duration);
        durationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, duration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /durations : get all the durations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of durations in body
     */
    @GetMapping("/durations")
    @Timed
    public List<Duration> getAllDurations() {
        log.debug("REST request to get all Durations");
        List<Duration> durations = durationRepository.findAll();
        return durations;
    }

    /**
     * GET  /durations/:id : get the "id" duration.
     *
     * @param id the id of the duration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the duration, or with status 404 (Not Found)
     */
    @GetMapping("/durations/{id}")
    @Timed
    public ResponseEntity<Duration> getDuration(@PathVariable Long id) {
        log.debug("REST request to get Duration : {}", id);
        Duration duration = durationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(duration));
    }

    /**
     * DELETE  /durations/:id : delete the "id" duration.
     *
     * @param id the id of the duration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/durations/{id}")
    @Timed
    public ResponseEntity<Void> deleteDuration(@PathVariable Long id) {
        log.debug("REST request to delete Duration : {}", id);
        durationRepository.delete(id);
        durationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/durations?query=:query : search for the duration corresponding
     * to the query.
     *
     * @param query the query of the duration search 
     * @return the result of the search
     */
    @GetMapping("/_search/durations")
    @Timed
    public List<Duration> searchDurations(@RequestParam String query) {
        log.debug("REST request to search Durations for query {}", query);
        return StreamSupport
            .stream(durationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
