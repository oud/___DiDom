package com.didom.myapp.repository.search;

import com.didom.myapp.domain.Proposal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Proposal entity.
 */
public interface ProposalSearchRepository extends ElasticsearchRepository<Proposal, Long> {
}
