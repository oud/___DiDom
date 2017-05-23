package com.didom.myapp.repository.search;

import com.didom.myapp.domain.PaymentType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentType entity.
 */
public interface PaymentTypeSearchRepository extends ElasticsearchRepository<PaymentType, Long> {
}
