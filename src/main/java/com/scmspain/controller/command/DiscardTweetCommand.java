package com.scmspain.controller.command;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class DiscardTweetCommand {
    @NotNull(message = "{tweet.notNull}")
    private Long tweet;

    public Long getTweet() {
        return tweet;
    }

    public void setTweet(Long tweet) {
        this.tweet = tweet;
    }
}
