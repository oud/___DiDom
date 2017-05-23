package com.didom.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PaymentType.
 */
@Entity
@Table(name = "payment_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "paymenttype")
public class PaymentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type_name", nullable = false)
    private String typeName;

    @OneToMany(mappedBy = "paymentType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "paymentType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Proposal> proposals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public PaymentType typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public PaymentType jobs(Set<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public PaymentType addJob(Job job) {
        this.jobs.add(job);
        job.setPaymentType(this);
        return this;
    }

    public PaymentType removeJob(Job job) {
        this.jobs.remove(job);
        job.setPaymentType(null);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public Set<Proposal> getProposals() {
        return proposals;
    }

    public PaymentType proposals(Set<Proposal> proposals) {
        this.proposals = proposals;
        return this;
    }

    public PaymentType addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setPaymentType(this);
        return this;
    }

    public PaymentType removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setPaymentType(null);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        this.proposals = proposals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentType paymentType = (PaymentType) o;
        if (paymentType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentType{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            "}";
    }
}
