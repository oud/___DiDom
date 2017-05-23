package com.didom.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.didom.myapp.domain.HasSkill;

import com.didom.myapp.repository.HasSkillRepository;
import com.didom.myapp.repository.search.HasSkillSearchRepository;
import com.didom.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing HasSkill.
 */
@RestController
@RequestMapping("/api")
public class HasSkillResource {

    private final Logger log = LoggerFactory.getLogger(HasSkillResource.class);

    private static final String ENTITY_NAME = "hasSkill";
        
    private final HasSkillRepository hasSkillRepository;

    private final HasSkillSearchRepository hasSkillSearchRepository;

    public HasSkillResource(HasSkillRepository hasSkillRepository, HasSkillSearchRepository hasSkillSearchRepository) {
        this.hasSkillRepository = hasSkillRepository;
        this.hasSkillSearchRepository = hasSkillSearchRepository;
    }

    /**
     * POST  /has-skills : Create a new hasSkill.
     *
     * @param hasSkill the hasSkill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hasSkill, or with status 400 (Bad Request) if the hasSkill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/has-skills")
    @Timed
    public ResponseEntity<HasSkill> createHasSkill(@RequestBody HasSkill hasSkill) throws URISyntaxException {
        log.debug("REST request to save HasSkill : {}", hasSkill);
        if (hasSkill.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hasSkill cannot already have an ID")).body(null);
        }
        HasSkill result = hasSkillRepository.save(hasSkill);
        hasSkillSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/has-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /has-skills : Updates an existing hasSkill.
     *
     * @param hasSkill the hasSkill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hasSkill,
     * or with status 400 (Bad Request) if the hasSkill is not valid,
     * or with status 500 (Internal Server Error) if the hasSkill couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/has-skills")
    @Timed
    public ResponseEntity<HasSkill> updateHasSkill(@RequestBody HasSkill hasSkill) throws URISyntaxException {
        log.debug("REST request to update HasSkill : {}", hasSkill);
        if (hasSkill.getId() == null) {
            return createHasSkill(hasSkill);
        }
        HasSkill result = hasSkillRepository.save(hasSkill);
        hasSkillSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hasSkill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /has-skills : get all the hasSkills.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hasSkills in body
     */
    @GetMapping("/has-skills")
    @Timed
    public List<HasSkill> getAllHasSkills() {
        log.debug("REST request to get all HasSkills");
        List<HasSkill> hasSkills = hasSkillRepository.findAll();
        return hasSkills;
    }

    /**
     * GET  /has-skills/:id : get the "id" hasSkill.
     *
     * @param id the id of the hasSkill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hasSkill, or with status 404 (Not Found)
     */
    @GetMapping("/has-skills/{id}")
    @Timed
    public ResponseEntity<HasSkill> getHasSkill(@PathVariable Long id) {
        log.debug("REST request to get HasSkill : {}", id);
        HasSkill hasSkill = hasSkillRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hasSkill));
    }

    /**
     * DELETE  /has-skills/:id : delete the "id" hasSkill.
     *
     * @param id the id of the hasSkill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/has-skills/{id}")
    @Timed
    public ResponseEntity<Void> deleteHasSkill(@PathVariable Long id) {
        log.debug("REST request to delete HasSkill : {}", id);
        hasSkillRepository.delete(id);
        hasSkillSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/has-skills?query=:query : search for the hasSkill corresponding
     * to the query.
     *
     * @param query the query of the hasSkill search 
     * @return the result of the search
     */
    @GetMapping("/_search/has-skills")
    @Timed
    public List<HasSkill> searchHasSkills(@RequestParam String query) {
        log.debug("REST request to search HasSkills for query {}", query);
        return StreamSupport
            .stream(hasSkillSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
