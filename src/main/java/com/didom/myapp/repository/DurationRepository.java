package com.didom.myapp.repository;

import com.didom.myapp.domain.Duration;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Duration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DurationRepository extends JpaRepository<Duration,Long> {

}
