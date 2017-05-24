package com.didom.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.didom.myapp.domain.HasSkill;
import com.didom.myapp.service.HasSkillService;
import com.didom.myapp.web.rest.util.HeaderUtil;
import com.didom.myapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
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
        
    private final HasSkillService hasSkillService;

    public HasSkillResource(HasSkillService hasSkillService) {
        this.hasSkillService = hasSkillService;
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
        HasSkill result = hasSkillService.save(hasSkill);
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
        HasSkill result = hasSkillService.save(hasSkill);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hasSkill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /has-skills : get all the hasSkills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hasSkills in body
     */
    @GetMapping("/has-skills")
    @Timed
    public ResponseEntity<List<HasSkill>> getAllHasSkills(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of HasSkills");
        Page<HasSkill> page = hasSkillService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/has-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
        HasSkill hasSkill = hasSkillService.findOne(id);
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
        hasSkillService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/has-skills?query=:query : search for the hasSkill corresponding
     * to the query.
     *
     * @param query the query of the hasSkill search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/has-skills")
    @Timed
    public ResponseEntity<List<HasSkill>> searchHasSkills(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of HasSkills for query {}", query);
        Page<HasSkill> page = hasSkillService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/has-skills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
