package com.didom.myapp.repository;

import com.didom.myapp.domain.Job;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job,Long> {

    @Query("select job from Job job where job.user.login = ?#{principal.username}")
    List<Job> findByUserIsCurrentUser();

}
