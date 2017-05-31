package com.scmspain.entities;

public class Tweet {
    private Long id;
    private String publisher;
    private String tweet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", publisher='" + publisher + '\'' +
                ", tweet='" + tweet + '\'' +
                '}';
    }
}
