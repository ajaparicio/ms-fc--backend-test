package com.scmspain.controller.command;

import com.scmspain.controller.validation.ValidateTweet;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class PublishTweetCommand {
    @NotEmpty (message = "{publisher.notEmpty}")
    private String publisher;

    @NotEmpty (message = "{tweet.notEmpty}")
    @ValidateTweet
    private String tweet;

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
}
