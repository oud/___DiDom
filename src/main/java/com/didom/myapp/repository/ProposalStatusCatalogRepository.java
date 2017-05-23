package com.didom.myapp.repository;

import com.didom.myapp.domain.ProposalStatusCatalog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProposalStatusCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProposalStatusCatalogRepository extends JpaRepository<ProposalStatusCatalog,Long> {

}
