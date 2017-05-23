package com.didom.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.didom.myapp.domain.PaymentType;

import com.didom.myapp.repository.PaymentTypeRepository;
import com.didom.myapp.repository.search.PaymentTypeSearchRepository;
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
 * REST controller for managing PaymentType.
 */
@RestController
@RequestMapping("/api")
public class PaymentTypeResource {

    private final Logger log = LoggerFactory.getLogger(PaymentTypeResource.class);

    private static final String ENTITY_NAME = "paymentType";
        
    private final PaymentTypeRepository paymentTypeRepository;

    private final PaymentTypeSearchRepository paymentTypeSearchRepository;

    public PaymentTypeResource(PaymentTypeRepository paymentTypeRepository, PaymentTypeSearchRepository paymentTypeSearchRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.paymentTypeSearchRepository = paymentTypeSearchRepository;
    }

    /**
     * POST  /payment-types : Create a new paymentType.
     *
     * @param paymentType the paymentType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentType, or with status 400 (Bad Request) if the paymentType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-types")
    @Timed
    public ResponseEntity<PaymentType> createPaymentType(@Valid @RequestBody PaymentType paymentType) throws URISyntaxException {
        log.debug("REST request to save PaymentType : {}", paymentType);
        if (paymentType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new paymentType cannot already have an ID")).body(null);
        }
        PaymentType result = paymentTypeRepository.save(paymentType);
        paymentTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/payment-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-types : Updates an existing paymentType.
     *
     * @param paymentType the paymentType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentType,
     * or with status 400 (Bad Request) if the paymentType is not valid,
     * or with status 500 (Internal Server Error) if the paymentType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-types")
    @Timed
    public ResponseEntity<PaymentType> updatePaymentType(@Valid @RequestBody PaymentType paymentType) throws URISyntaxException {
        log.debug("REST request to update PaymentType : {}", paymentType);
        if (paymentType.getId() == null) {
            return createPaymentType(paymentType);
        }
        PaymentType result = paymentTypeRepository.save(paymentType);
        paymentTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-types : get all the paymentTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paymentTypes in body
     */
    @GetMapping("/payment-types")
    @Timed
    public List<PaymentType> getAllPaymentTypes() {
        log.debug("REST request to get all PaymentTypes");
        List<PaymentType> paymentTypes = paymentTypeRepository.findAll();
        return paymentTypes;
    }

    /**
     * GET  /payment-types/:id : get the "id" paymentType.
     *
     * @param id the id of the paymentType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentType, or with status 404 (Not Found)
     */
    @GetMapping("/payment-types/{id}")
    @Timed
    public ResponseEntity<PaymentType> getPaymentType(@PathVariable Long id) {
        log.debug("REST request to get PaymentType : {}", id);
        PaymentType paymentType = paymentTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentType));
    }

    /**
     * DELETE  /payment-types/:id : delete the "id" paymentType.
     *
     * @param id the id of the paymentType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentType(@PathVariable Long id) {
        log.debug("REST request to delete PaymentType : {}", id);
        paymentTypeRepository.delete(id);
        paymentTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/payment-types?query=:query : search for the paymentType corresponding
     * to the query.
     *
     * @param query the query of the paymentType search 
     * @return the result of the search
     */
    @GetMapping("/_search/payment-types")
    @Timed
    public List<PaymentType> searchPaymentTypes(@RequestParam String query) {
        log.debug("REST request to search PaymentTypes for query {}", query);
        return StreamSupport
            .stream(paymentTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
