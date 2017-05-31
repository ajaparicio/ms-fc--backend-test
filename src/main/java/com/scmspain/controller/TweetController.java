package com.scmspain.controller;

import com.scmspain.controller.command.PublishTweetCommand;
import com.scmspain.entities.Tweet;
import com.scmspain.services.TweetService;
import com.scmspain.services.dao.TweetDAO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class TweetController {
    private TweetService tweetService;

    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/tweet")
    public List<Tweet> listAllTweets() {
        final List<Tweet> tweets = new ArrayList<>();
        //TODO: Use Dozer for that
        Tweet tweet;
        for (TweetDAO dao : this.tweetService.listAllTweets()) {
            tweet = new Tweet();
            tweet.setId(dao.getId());
            tweet.setPublisher(dao.getPublisher());
            tweet.setTweet(dao.getTweet());

            tweets.add(tweet);
        }
        return tweets;
    }

    @PostMapping("/tweet")
    @ResponseStatus(CREATED)
    public void publishTweet(@Valid @RequestBody PublishTweetCommand publishTweetCommand) {
        this.tweetService.publishTweet(publishTweetCommand.getPublisher(), publishTweetCommand.getTweet());
    }
}
