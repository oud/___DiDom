package com.didom.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.didom.myapp.domain.ProposalStatusCatalog;

import com.didom.myapp.repository.ProposalStatusCatalogRepository;
import com.didom.myapp.repository.search.ProposalStatusCatalogSearchRepository;
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
 * REST controller for managing ProposalStatusCatalog.
 */
@RestController
@RequestMapping("/api")
public class ProposalStatusCatalogResource {

    private final Logger log = LoggerFactory.getLogger(ProposalStatusCatalogResource.class);

    private static final String ENTITY_NAME = "proposalStatusCatalog";
        
    private final ProposalStatusCatalogRepository proposalStatusCatalogRepository;

    private final ProposalStatusCatalogSearchRepository proposalStatusCatalogSearchRepository;

    public ProposalStatusCatalogResource(ProposalStatusCatalogRepository proposalStatusCatalogRepository, ProposalStatusCatalogSearchRepository proposalStatusCatalogSearchRepository) {
        this.proposalStatusCatalogRepository = proposalStatusCatalogRepository;
        this.proposalStatusCatalogSearchRepository = proposalStatusCatalogSearchRepository;
    }

    /**
     * POST  /proposal-status-catalogs : Create a new proposalStatusCatalog.
     *
     * @param proposalStatusCatalog the proposalStatusCatalog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proposalStatusCatalog, or with status 400 (Bad Request) if the proposalStatusCatalog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/proposal-status-catalogs")
    @Timed
    public ResponseEntity<ProposalStatusCatalog> createProposalStatusCatalog(@RequestBody ProposalStatusCatalog proposalStatusCatalog) throws URISyntaxException {
        log.debug("REST request to save ProposalStatusCatalog : {}", proposalStatusCatalog);
        if (proposalStatusCatalog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new proposalStatusCatalog cannot already have an ID")).body(null);
        }
        ProposalStatusCatalog result = proposalStatusCatalogRepository.save(proposalStatusCatalog);
        proposalStatusCatalogSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/proposal-status-catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /proposal-status-catalogs : Updates an existing proposalStatusCatalog.
     *
     * @param proposalStatusCatalog the proposalStatusCatalog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proposalStatusCatalog,
     * or with status 400 (Bad Request) if the proposalStatusCatalog is not valid,
     * or with status 500 (Internal Server Error) if the proposalStatusCatalog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/proposal-status-catalogs")
    @Timed
    public ResponseEntity<ProposalStatusCatalog> updateProposalStatusCatalog(@RequestBody ProposalStatusCatalog proposalStatusCatalog) throws URISyntaxException {
        log.debug("REST request to update ProposalStatusCatalog : {}", proposalStatusCatalog);
        if (proposalStatusCatalog.getId() == null) {
            return createProposalStatusCatalog(proposalStatusCatalog);
        }
        ProposalStatusCatalog result = proposalStatusCatalogRepository.save(proposalStatusCatalog);
        proposalStatusCatalogSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, proposalStatusCatalog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /proposal-status-catalogs : get all the proposalStatusCatalogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of proposalStatusCatalogs in body
     */
    @GetMapping("/proposal-status-catalogs")
    @Timed
    public List<ProposalStatusCatalog> getAllProposalStatusCatalogs() {
        log.debug("REST request to get all ProposalStatusCatalogs");
        List<ProposalStatusCatalog> proposalStatusCatalogs = proposalStatusCatalogRepository.findAll();
        return proposalStatusCatalogs;
    }

    /**
     * GET  /proposal-status-catalogs/:id : get the "id" proposalStatusCatalog.
     *
     * @param id the id of the proposalStatusCatalog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proposalStatusCatalog, or with status 404 (Not Found)
     */
    @GetMapping("/proposal-status-catalogs/{id}")
    @Timed
    public ResponseEntity<ProposalStatusCatalog> getProposalStatusCatalog(@PathVariable Long id) {
        log.debug("REST request to get ProposalStatusCatalog : {}", id);
        ProposalStatusCatalog proposalStatusCatalog = proposalStatusCatalogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(proposalStatusCatalog));
    }

    /**
     * DELETE  /proposal-status-catalogs/:id : delete the "id" proposalStatusCatalog.
     *
     * @param id the id of the proposalStatusCatalog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/proposal-status-catalogs/{id}")
    @Timed
    public ResponseEntity<Void> deleteProposalStatusCatalog(@PathVariable Long id) {
        log.debug("REST request to delete ProposalStatusCatalog : {}", id);
        proposalStatusCatalogRepository.delete(id);
        proposalStatusCatalogSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/proposal-status-catalogs?query=:query : search for the proposalStatusCatalog corresponding
     * to the query.
     *
     * @param query the query of the proposalStatusCatalog search 
     * @return the result of the search
     */
    @GetMapping("/_search/proposal-status-catalogs")
    @Timed
    public List<ProposalStatusCatalog> searchProposalStatusCatalogs(@RequestParam String query) {
        log.debug("REST request to search ProposalStatusCatalogs for query {}", query);
        return StreamSupport
            .stream(proposalStatusCatalogSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
