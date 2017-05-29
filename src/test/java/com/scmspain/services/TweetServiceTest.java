package com.scmspain.services;

import com.scmspain.entities.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TweetServiceTest {
    private EntityManager entityManager;
    private MetricWriter metricWriter;

    private TweetService tweetService;

    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.metricWriter = mock(MetricWriter.class);

        this.tweetService = new TweetService(entityManager, metricWriter);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        verify(entityManager).persist(any(Tweet.class));
        verify(metricWriter).increment(any(Delta.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenPublisherIsEmpty() throws Exception {
        try {
            tweetService.publishTweet("", "Any valid message");
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenPublisherIsNull() throws Exception {
        try {
            tweetService.publishTweet(null, "Any valid message");
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        try {
            tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetIsEmpty() throws Exception {
        try {
            tweetService.publishTweet("Pirate", "");
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetIsNull() throws Exception {
        try {
            tweetService.publishTweet("Pirate", null);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test
    public void shouldReturnATweet() {
        Long anyId = 1L;
        Tweet anyTweet = new Tweet();
        when(entityManager.find(Tweet.class, anyId)).thenReturn(anyTweet);

        Tweet tweet = tweetService.getTweet(1L);

        assertThat(tweet).isEqualTo(anyTweet);

        verify(entityManager).find(Tweet.class, anyId);
    }

    @Test
    public void shouldReturnNullWhenNotExistTheId() {
        Long anyId = 1L;
        when(entityManager.find(Tweet.class, anyId)).thenReturn(null);

        Tweet tweet = tweetService.getTweet(1L);

        assertThat(tweet).isNull();

        verify(entityManager).find(Tweet.class, anyId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenIdIsNull() throws Exception {
        try {
            tweetService.getTweet(null);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    public void shouldReturnAllTweet() {
        Tweet anyTweet = new Tweet();

        TypedQuery<Tweet> anyQuery = mock(TypedQuery.class);
        when(anyQuery.getResultList()).thenReturn(Arrays.asList(anyTweet));
        when(entityManager.createQuery("SELECT t FROM Tweet t WHERE pre2015MigrationStatus<>99 ORDER BY id DESC", Tweet.class)).thenReturn(anyQuery);

        List<Tweet> tweets = tweetService.listAllTweets();

        assertThat(tweets.size()).isEqualTo(1);
        assertThat(tweets.contains(anyTweet)).isTrue();

        verify(metricWriter).increment(any(Delta.class));
    }
}
