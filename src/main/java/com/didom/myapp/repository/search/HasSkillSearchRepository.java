package com.didom.myapp.repository.search;

import com.didom.myapp.domain.HasSkill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HasSkill entity.
 */
public interface HasSkillSearchRepository extends ElasticsearchRepository<HasSkill, Long> {
}
