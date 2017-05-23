package com.didom.myapp.repository.search;

import com.didom.myapp.domain.ProposalStatusCatalog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProposalStatusCatalog entity.
 */
public interface ProposalStatusCatalogSearchRepository extends ElasticsearchRepository<ProposalStatusCatalog, Long> {
}
