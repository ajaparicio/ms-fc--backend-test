package com.scmspain.controller;

import com.scmspain.controller.command.DiscardTweetCommand;
import com.scmspain.controller.command.PublishTweetCommand;
import com.scmspain.controller.mapper.TweetMapper;
import com.scmspain.entities.Tweet;
import com.scmspain.services.TweetService;
import com.scmspain.dao.TweetDAO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class TweetController {
    private TweetService tweetService;
    private TweetMapper tweetMapper;

    public TweetController(TweetService tweetService, TweetMapper tweetMapper) {
        this.tweetService = tweetService;
        this.tweetMapper = tweetMapper;
    }

    @GetMapping("/tweet")
    public List<Tweet> listAllTweets() {
        return tweetMapper.map(this.tweetService.listAllTweets());
    }

    @PostMapping("/tweet")
    @ResponseStatus(CREATED)
    public void publishTweet(@Valid @RequestBody PublishTweetCommand publishTweetCommand) {
        final TweetUtils utils = new TweetUtils();
        final List<String> links = utils.getLinks(publishTweetCommand.getTweet());
        final String message = utils.getMessageWithoutLinks(publishTweetCommand.getTweet());

        this.tweetService.publishTweet(publishTweetCommand.getPublisher(), message, links);
    }

    @PostMapping("/discarded")
    @ResponseStatus(ACCEPTED)
    public void discardTweet(@Valid @RequestBody DiscardTweetCommand discardTweetCommand) {
        this.tweetService.discardTweet(discardTweetCommand.getTweet());
    }

    @GetMapping("/discarded/{tweetId}")
    public void discardTweet(@PathVariable  Long tweetId) {
        this.tweetService.discardTweet(tweetId);
    }

    @GetMapping("/discarded")
    public List<Tweet> listAllDiscardedTweets() {
        return tweetMapper.map(this.tweetService.listAllDiscardedTweets());
    }
}
