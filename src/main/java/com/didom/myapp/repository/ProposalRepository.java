package com.didom.myapp.repository;

import com.didom.myapp.domain.Proposal;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Proposal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProposalRepository extends JpaRepository<Proposal,Long> {

    @Query("select proposal from Proposal proposal where proposal.user.login = ?#{principal.username}")
    List<Proposal> findByUserIsCurrentUser();

}
