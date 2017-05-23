package com.didom.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_time")
    private ZonedDateTime messageTime;

    @Column(name = "message_text")
    private String messageText;

    @ManyToOne
    private Proposal proposal;

    @ManyToOne
    private ProposalStatusCatalog proposalStatusCatalog;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "message_user",
               joinColumns = @JoinColumn(name="messages_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="id"))
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "message")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attachment> attachments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getMessageTime() {
        return messageTime;
    }

    public Message messageTime(ZonedDateTime messageTime) {
        this.messageTime = messageTime;
        return this;
    }

    public void setMessageTime(ZonedDateTime messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public Message messageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public Message proposal(Proposal proposal) {
        this.proposal = proposal;
        return this;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public ProposalStatusCatalog getProposalStatusCatalog() {
        return proposalStatusCatalog;
    }

    public Message proposalStatusCatalog(ProposalStatusCatalog proposalStatusCatalog) {
        this.proposalStatusCatalog = proposalStatusCatalog;
        return this;
    }

    public void setProposalStatusCatalog(ProposalStatusCatalog proposalStatusCatalog) {
        this.proposalStatusCatalog = proposalStatusCatalog;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Message users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Message addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Message removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public Message attachments(Set<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Message addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setMessage(this);
        return this;
    }

    public Message removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setMessage(null);
        return this;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if (message.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", messageTime='" + getMessageTime() + "'" +
            ", messageText='" + getMessageText() + "'" +
            "}";
    }
}
