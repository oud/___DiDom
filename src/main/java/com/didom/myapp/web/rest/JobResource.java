package com.didom.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.didom.myapp.domain.*;
import com.didom.myapp.domain.enumeration.TypeUser;
import com.didom.myapp.repository.AuthorityRepository;
import com.didom.myapp.repository.ProposalStatusCatalogRepository;
import com.didom.myapp.repository.UserInfoRepository;
import com.didom.myapp.repository.UserRepository;
import com.didom.myapp.security.AuthoritiesConstants;
import com.didom.myapp.service.JobService;
import com.didom.myapp.service.ProposalService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.ZonedDateTime.now;

/**
 * REST controller for managing Job.
 */
@RestController
@RequestMapping("/api")
public class JobResource {

    private final Logger log = LoggerFactory.getLogger(JobResource.class);

    private static final String ENTITY_NAME = "job";

    private final JobService jobService;

    private final ProposalService proposalService;

    private final ProposalStatusCatalogRepository proposalStatusCatalogRepository;

    private final UserInfoRepository userInfoRepository;
    private final AuthorityRepository authorityRepository;

    public JobResource(JobService jobService, ProposalService proposalService, ProposalStatusCatalogRepository proposalStatusCatalogRepository, UserRepository userRepository, UserInfoRepository userInfoRepository, AuthorityRepository authorityRepository) {
        this.jobService = jobService;
        this.proposalService = proposalService;
        this.proposalStatusCatalogRepository = proposalStatusCatalogRepository;
        this.userInfoRepository = userInfoRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * POST  /jobs : Create a new job.
     *
     * @param job the job to create
     * @return the ResponseEntity with status 201 (Created) and with body the new job, or with status 400 (Bad Request) if the job has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jobs")
    @Timed
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to save Job : {}", job);
        if (job.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new job cannot already have an ID")).body(null);
        }
        Job result = jobService.save(job);
        List<UserInfo> userList = userInfoRepository.findAll();
        for (UserInfo userInfo : userList){
            //Authority authority = authorityRepository.findOne(AuthoritiesConstants.SEEKER);
            if(userInfo.getUserType().equals(TypeUser.SEEKER) && userInfo.getUser().getActivated()== true){
                Proposal proposal = new Proposal();
                Proposal result1= proposalService.save(proposal
                    .currentProposalStatus(proposalStatusCatalogRepository.findOne((long) 1))
                    .proposalTime(new Date().toInstant().atZone(ZoneId.systemDefault()))
                    .job(job)
                    .paymentAmount(job.getPaymentAmont())
                    .paymentType(job.getPaymentType())
                    .user(userInfo.getUser()));
            }
        }
        return ResponseEntity.created(new URI("/api/jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobs : Updates an existing job.
     *
     * @param job the job to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated job,
     * or with status 400 (Bad Request) if the job is not valid,
     * or with status 500 (Internal Server Error) if the job couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jobs")
    @Timed
    public ResponseEntity<Job> updateJob(@Valid @RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to update Job : {}", job);
        if (job.getId() == null) {
            return createJob(job);
        }
        Job result = jobService.save(job);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, job.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobs : get all the jobs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
     */
    @GetMapping("/jobs")
    @Timed
    public ResponseEntity<List<Job>> getAllJobs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Jobs");
        Page<Job> page = jobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jobs/:id : get the "id" job.
     *
     * @param id the id of the job to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the job, or with status 404 (Not Found)
     */
    @GetMapping("/jobs/{id}")
    @Timed
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        log.debug("REST request to get Job : {}", id);
        Job job = jobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(job));
    }

    /**
     * DELETE  /jobs/:id : delete the "id" job.
     *
     * @param id the id of the job to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jobs/{id}")
    @Timed
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        log.debug("REST request to delete Job : {}", id);
        jobService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobs?query=:query : search for the job corresponding
     * to the query.
     *
     * @param query the query of the job search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/jobs")
    @Timed
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Jobs for query {}", query);
        Page<Job> page = jobService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
