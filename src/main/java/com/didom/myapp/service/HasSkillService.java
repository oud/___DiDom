package com.didom.myapp.service;

import com.didom.myapp.domain.HasSkill;
import com.didom.myapp.domain.User;
import com.didom.myapp.repository.HasSkillRepository;
import com.didom.myapp.repository.UserRepository;
import com.didom.myapp.repository.search.HasSkillSearchRepository;
import com.didom.myapp.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing HasSkill.
 */
@Service
@Transactional
public class HasSkillService {

    private final Logger log = LoggerFactory.getLogger(HasSkillService.class);

    private final HasSkillRepository hasSkillRepository;

    private final UserRepository userRepository;

    private final HasSkillSearchRepository hasSkillSearchRepository;

    public HasSkillService(HasSkillRepository hasSkillRepository, UserRepository userRepository, HasSkillSearchRepository hasSkillSearchRepository) {
        this.hasSkillRepository = hasSkillRepository;
        this.userRepository = userRepository;
        this.hasSkillSearchRepository = hasSkillSearchRepository;
    }

    /**
     * Save a hasSkill.
     *
     * @param hasSkill the entity to save
     * @return the persisted entity
     */
    public HasSkill save(HasSkill hasSkill) {
        log.debug("Request to save HasSkill : {}", hasSkill);
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (user.isPresent()) hasSkill.setUser(user.get());
        HasSkill result = hasSkillRepository.save(hasSkill);
        hasSkillSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the hasSkills.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HasSkill> findAll(Pageable pageable) {
        log.debug("Request to get all HasSkills");
        Page<HasSkill> result = hasSkillRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one hasSkill by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public HasSkill findOne(Long id) {
        log.debug("Request to get HasSkill : {}", id);
        HasSkill hasSkill = hasSkillRepository.findOne(id);
        return hasSkill;
    }

    /**
     *  Delete the  hasSkill by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HasSkill : {}", id);
        hasSkillRepository.delete(id);
        hasSkillSearchRepository.delete(id);
    }

    /**
     * Search for the hasSkill corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HasSkill> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HasSkills for query {}", query);
        Page<HasSkill> result = hasSkillSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
