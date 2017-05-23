package com.didom.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.didom.myapp.domain.enumeration.Complexity;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "payment_amont", precision=10, scale=2, nullable = false)
    private BigDecimal paymentAmont;

    @Enumerated(EnumType.STRING)
    @Column(name = "complexity")
    private Complexity complexity;

    @ManyToOne
    private Skill mainSkill;

    @ManyToOne
    private PaymentType paymentType;

    @ManyToOne
    private Duration duration;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "job")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Proposal> proposals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Job title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Job description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPaymentAmont() {
        return paymentAmont;
    }

    public Job paymentAmont(BigDecimal paymentAmont) {
        this.paymentAmont = paymentAmont;
        return this;
    }

    public void setPaymentAmont(BigDecimal paymentAmont) {
        this.paymentAmont = paymentAmont;
    }

    public Complexity getComplexity() {
        return complexity;
    }

    public Job complexity(Complexity complexity) {
        this.complexity = complexity;
        return this;
    }

    public void setComplexity(Complexity complexity) {
        this.complexity = complexity;
    }

    public Skill getMainSkill() {
        return mainSkill;
    }

    public Job mainSkill(Skill skill) {
        this.mainSkill = skill;
        return this;
    }

    public void setMainSkill(Skill skill) {
        this.mainSkill = skill;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public Job paymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Duration getDuration() {
        return duration;
    }

    public Job duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public User getUser() {
        return user;
    }

    public Job user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Proposal> getProposals() {
        return proposals;
    }

    public Job proposals(Set<Proposal> proposals) {
        this.proposals = proposals;
        return this;
    }

    public Job addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setJob(this);
        return this;
    }

    public Job removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setJob(null);
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
        Job job = (Job) o;
        if (job.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), job.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", paymentAmont='" + getPaymentAmont() + "'" +
            ", complexity='" + getComplexity() + "'" +
            "}";
    }
}
