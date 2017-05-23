package com.didom.myapp.service;

import com.didom.myapp.domain.Proposal;
import com.didom.myapp.repository.ProposalRepository;
import com.didom.myapp.repository.search.ProposalSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Proposal.
 */
@Service
@Transactional
public class ProposalService {

    private final Logger log = LoggerFactory.getLogger(ProposalService.class);
    
    private final ProposalRepository proposalRepository;

    private final ProposalSearchRepository proposalSearchRepository;

    public ProposalService(ProposalRepository proposalRepository, ProposalSearchRepository proposalSearchRepository) {
        this.proposalRepository = proposalRepository;
        this.proposalSearchRepository = proposalSearchRepository;
    }

    /**
     * Save a proposal.
     *
     * @param proposal the entity to save
     * @return the persisted entity
     */
    public Proposal save(Proposal proposal) {
        log.debug("Request to save Proposal : {}", proposal);
        Proposal result = proposalRepository.save(proposal);
        proposalSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the proposals.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Proposal> findAll(Pageable pageable) {
        log.debug("Request to get all Proposals");
        Page<Proposal> result = proposalRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one proposal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Proposal findOne(Long id) {
        log.debug("Request to get Proposal : {}", id);
        Proposal proposal = proposalRepository.findOne(id);
        return proposal;
    }

    /**
     *  Delete the  proposal by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Proposal : {}", id);
        proposalRepository.delete(id);
        proposalSearchRepository.delete(id);
    }

    /**
     * Search for the proposal corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Proposal> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Proposals for query {}", query);
        Page<Proposal> result = proposalSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
