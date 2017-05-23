package com.didom.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProposalStatusCatalog.
 */
@Entity
@Table(name = "proposal_status_catalog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "proposalstatuscatalog")
public class ProposalStatusCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_name")
    private String statusName;

    @OneToMany(mappedBy = "currentProposalStatus")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Proposal> proposals = new HashSet<>();

    @OneToMany(mappedBy = "proposalStatusCatalog")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> messages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public ProposalStatusCatalog statusName(String statusName) {
        this.statusName = statusName;
        return this;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Set<Proposal> getProposals() {
        return proposals;
    }

    public ProposalStatusCatalog proposals(Set<Proposal> proposals) {
        this.proposals = proposals;
        return this;
    }

    public ProposalStatusCatalog addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setCurrentProposalStatus(this);
        return this;
    }

    public ProposalStatusCatalog removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setCurrentProposalStatus(null);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        this.proposals = proposals;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public ProposalStatusCatalog messages(Set<Message> messages) {
        this.messages = messages;
        return this;
    }

    public ProposalStatusCatalog addMessage(Message message) {
        this.messages.add(message);
        message.setProposalStatusCatalog(this);
        return this;
    }

    public ProposalStatusCatalog removeMessage(Message message) {
        this.messages.remove(message);
        message.setProposalStatusCatalog(null);
        return this;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProposalStatusCatalog proposalStatusCatalog = (ProposalStatusCatalog) o;
        if (proposalStatusCatalog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), proposalStatusCatalog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProposalStatusCatalog{" +
            "id=" + getId() +
            ", statusName='" + getStatusName() + "'" +
            "}";
    }
}
