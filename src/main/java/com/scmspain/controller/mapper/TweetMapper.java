package com.scmspain.controller.mapper;

import com.scmspain.dao.LinkDAO;
import com.scmspain.dao.TweetDAO;
import com.scmspain.entities.Tweet;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.List;

//TODO: Use Dozer for that
public class TweetMapper {

    public List<Tweet> map(List<TweetDAO> list) {
        final List<Tweet> tweets = new ArrayList<>();

        for (TweetDAO dao : list) {
            tweets.add(map(dao));
        }
        return tweets;
    }

    public Tweet map(TweetDAO dao) {
        Tweet tweet = new Tweet();
        tweet.setId(dao.getId());
        tweet.setPublisher(dao.getPublisher());
        tweet.setTweet(getMessage(dao));
        tweet.setPre2015MigrationStatus(dao.getPre2015MigrationStatus());

        return tweet;
    }

    private String getMessage(TweetDAO dao) {
        StringBuilder stringBuilder = new StringBuilder(dao.getTweet()).append(' ');
        for (LinkDAO linkDAO : dao.getLinks()) {
            stringBuilder.append(linkDAO.getUrl()).append(' ');
        }
        return stringBuilder.toString();
    }
}
