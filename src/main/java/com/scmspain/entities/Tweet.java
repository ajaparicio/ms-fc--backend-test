package com.scmspain.entities;

public class Tweet {
    private Long id;
    private String publisher;
    private String tweet;
    private Long pre2015MigrationStatus;

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

    public Long getPre2015MigrationStatus() {
        return pre2015MigrationStatus;
    }

    public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
        this.pre2015MigrationStatus = pre2015MigrationStatus;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", publisher='" + publisher + '\'' +
                ", tweet='" + tweet + '\'' +
                ", pre2015MigrationStatus=" + pre2015MigrationStatus +
                '}';
    }
}
