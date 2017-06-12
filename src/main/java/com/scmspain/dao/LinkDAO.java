package com.scmspain.dao;

import com.scmspain.entities.Tweet;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Link")
public class LinkDAO implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tweet_fk", nullable = false)
    private TweetDAO tweet;

    public LinkDAO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TweetDAO getTweet() {
        return tweet;
    }

    public void setTweet(TweetDAO tweet) {
        this.tweet = tweet;
    }

    @Override
    public String toString() {
        return "LinkDAO{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", tweet=" + tweet +
                '}';
    }
}
