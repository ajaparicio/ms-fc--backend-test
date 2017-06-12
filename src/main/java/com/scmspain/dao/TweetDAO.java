package com.scmspain.dao;

import com.scmspain.entities.Tweet;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity(name = "Tweet")
public class TweetDAO implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false, length = 140)
    private String tweet;
    @Column (nullable=true)
    private Long pre2015MigrationStatus = 0L;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "discarded_date", nullable=true)
    private Date discardedDate;
    @Column (columnDefinition = "boolean default false", nullable = false)
    private boolean discarded = false;
    @OneToMany(fetch = FetchType.EAGER, cascade = ALL, mappedBy="tweet")
    private List<LinkDAO> links;

    public TweetDAO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Long getPre2015MigrationStatus() {
        return pre2015MigrationStatus;
    }

    public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
        this.pre2015MigrationStatus = pre2015MigrationStatus;
    }

    public Date getDiscardedDate() {
        return discardedDate;
    }

    public void setDiscardedDate(Date discardedDate) {
        this.discardedDate = discardedDate;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public List<LinkDAO> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDAO> links) {
        this.links = links;
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @Override
    public String toString() {
        return "TweetDAO{" +
                "id=" + id +
                ", created=" + created +
                ", publisher='" + publisher + '\'' +
                ", tweet='" + tweet + '\'' +
                ", pre2015MigrationStatus=" + pre2015MigrationStatus +
                ", discardedDate=" + discardedDate +
                ", discarded=" + discarded +
                ", links=" + links +
                '}';
    }
}
