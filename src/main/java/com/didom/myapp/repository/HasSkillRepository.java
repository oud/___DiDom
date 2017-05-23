package com.didom.myapp.repository;

import com.didom.myapp.domain.HasSkill;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the HasSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HasSkillRepository extends JpaRepository<HasSkill,Long> {

    @Query("select has_skill from HasSkill has_skill where has_skill.user.login = ?#{principal.username}")
    List<HasSkill> findByUserIsCurrentUser();

}
