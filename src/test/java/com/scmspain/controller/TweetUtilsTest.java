package com.scmspain.controller;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TweetUtilsTest {
    @Test
    public void getLinksFromTweetWithoutLinks() {
        List<String> links = new TweetUtils().getLinks("No links in the message");

        assertTrue(links.isEmpty());
    }

    @Test
    public void getLinksFromTweetWithLinks() {
        List<String> links = new TweetUtils().getLinks("Links in the message http://one.org https://two.com ");

        assertEquals(links.size(), 2);
        assertTrue(links.contains("http://one.org"));
        assertTrue(links.contains("https://two.com"));
    }

    @Test
    public void getLinksFromTweetWithLinksAndTextInTheMiddle() {
        List<String> links = new TweetUtils().getLinks("Links in the message http://one.org text in the middle https://two.com ");

        assertEquals(links.size(), 2);
        assertTrue(links.contains("http://one.org"));
        assertTrue(links.contains("https://two.com"));
    }

    @Test
    public void getLinksFromTweetLinkWithoutSpaceInTheEnd() {
        List<String> links = new TweetUtils().getLinks("Links in the message http://one.org");

        assertTrue(links.isEmpty());
    }

    @Test
    public void getMessageFromTweetWithoutLinks() {
        String message = new TweetUtils().getMessageWithoutLinks("No links in the message");

        assertEquals("No links in the message", message);
    }

    @Test
    public void getMessageFromTweetWithtLinks() {
        String message = new TweetUtils().getMessageWithoutLinks("Links in the message http://one.org https://two.com ");

        assertEquals("Links in the message ", message);
    }

    @Test
    public void getMessageFromTweetWithLinksAndTextInTheMiddle() {
        String message = new TweetUtils().getMessageWithoutLinks("Links in the message http://one.org text in the middle https://two.com ");

        assertEquals("Links in the message text in the middle ", message);
    }

    @Test
    public void getMessageFromTweetLinkWithoutSpaceInTheEnd() {
        String message = new TweetUtils().getMessageWithoutLinks("Links in the message http://one.org");

        assertEquals( "Links in the message http://one.org", message);
    }
}
