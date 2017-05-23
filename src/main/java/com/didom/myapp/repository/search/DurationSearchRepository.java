package com.didom.myapp.repository.search;

import com.didom.myapp.domain.Duration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Duration entity.
 */
public interface DurationSearchRepository extends ElasticsearchRepository<Duration, Long> {
}
