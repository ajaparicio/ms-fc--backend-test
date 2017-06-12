package com.scmspain.services;

import com.scmspain.entities.Tweet;
import com.scmspain.dao.TweetDAO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.Arrays;
import java.util.Collections;
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
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.", Collections.EMPTY_LIST);

        verify(entityManager).persist(any(Tweet.class));
        verify(metricWriter).increment(any(Delta.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenPublisherIsEmpty() throws Exception {
        try {
            tweetService.publishTweet("", "Any valid message", Collections.EMPTY_LIST);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenPublisherIsNull() throws Exception {
        try {
            tweetService.publishTweet(null, "Any valid message", Collections.EMPTY_LIST);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        try {
            tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.", Collections.EMPTY_LIST);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetIsEmpty() throws Exception {
        try {
            tweetService.publishTweet("Pirate", "", Collections.EMPTY_LIST);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenTweetIsNull() throws Exception {
        try {
            tweetService.publishTweet("Pirate", null, Collections.EMPTY_LIST);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test
    public void shouldReturnATweet() {
        Long anyId = 1L;
        TweetDAO anyTweet = new TweetDAO();
        when(entityManager.find(TweetDAO.class, anyId)).thenReturn(anyTweet);

        TweetDAO tweet = tweetService.getTweet(1L);

        assertThat(tweet).isEqualTo(anyTweet);

        verify(entityManager).find(TweetDAO.class, anyId);
    }

    @Test
    public void shouldReturnNullWhenNotExistTheId() {
        Long anyId = 1L;
        when(entityManager.find(TweetDAO.class, anyId)).thenReturn(null);

        TweetDAO tweet = tweetService.getTweet(1L);

        assertThat(tweet).isNull();

        verify(entityManager).find(TweetDAO.class, anyId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenIdIsNull() throws Exception {
        try {
            tweetService.getTweet(null);
        } finally {
            verify(metricWriter, never()).increment(any(Delta.class));
        }
    }

    @Test
    public void shouldReturnAllTweet() {
        TweetDAO anyTweet = new TweetDAO();

        TypedQuery<TweetDAO> anyQuery = mock(TypedQuery.class);
        when(anyQuery.getResultList()).thenReturn(Arrays.asList(anyTweet));
        when(entityManager.createQuery("SELECT t FROM Tweet t WHERE pre2015MigrationStatus<>99 ORDER BY created DESC", TweetDAO.class)).thenReturn(anyQuery);

        List<TweetDAO> tweets = tweetService.listAllTweets();

        assertThat(tweets.size()).isEqualTo(1);
        assertThat(tweets.contains(anyTweet)).isTrue();

        verify(metricWriter).increment(any(Delta.class));
    }

    @Test
    public void shouldReturnAllDiscardedTweet() {
        TweetDAO anyTweet = new TweetDAO();

        CriteriaBuilder builder = mock(CriteriaBuilder.class);
        TypedQuery query = mock(TypedQuery.class);
        CriteriaQuery<TweetDAO> criteriaQuery = mock(CriteriaQuery.class);
        Root<TweetDAO> root = mock(Root.class);
        Path path = mock(Path.class);
        Predicate discardedTrue = mock(Predicate.class);
        Predicate notPre2015MigrationStatus = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(TweetDAO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(TweetDAO.class)).thenReturn(root);
        when(root.get(anyString())).thenReturn(path);
        when(query.getResultList()).thenReturn(Arrays.asList(anyTweet));
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(query);

        List<TweetDAO> tweets = tweetService.listAllDiscardedTweets();

        assertThat(tweets.size()).isEqualTo(1);
        assertThat(tweets.contains(anyTweet)).isTrue();

        verify(metricWriter).increment(any(Delta.class));
        verify(criteriaQuery).from(TweetDAO.class);
        verify(builder).equal(root.get("discarded"), true);
        verify(builder).notEqual(root.get("pre2015MigrationStatus"), 99);
        verify(criteriaQuery).where(builder.and(discardedTrue, notPre2015MigrationStatus));
    }

    @Test
    public void shouldDiscardedOneTweet() {
        Long anyId = 1L;

        CriteriaBuilder builder = mock(CriteriaBuilder.class);
        Query query = mock(Query.class);
        CriteriaUpdate<TweetDAO> criteriaUpdate = mock(CriteriaUpdate.class);
        Root<TweetDAO> root = mock(Root.class);
        Path path = mock(Path.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createCriteriaUpdate(TweetDAO.class)).thenReturn(criteriaUpdate);
        when(criteriaUpdate.from(TweetDAO.class)).thenReturn(root);
        when(root.get(anyString())).thenReturn(path);
        when(entityManager.createQuery(criteriaUpdate)).thenReturn(query);

        tweetService.discardTweet(anyId);

        verify(metricWriter).increment(any(Delta.class));
        verify(criteriaUpdate).from(TweetDAO.class);
        verify(builder).equal(root.get("id"), anyId);
        verify(query).executeUpdate();
    }
}
