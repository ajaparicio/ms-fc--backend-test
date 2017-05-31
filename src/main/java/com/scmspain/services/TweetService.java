package com.scmspain.services;

import com.scmspain.entities.Tweet;
import com.scmspain.services.dao.TweetDAO;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TweetService {
    private final EntityManager entityManager;
    private final MetricWriter metricWriter;

    public TweetService(EntityManager entityManager, MetricWriter metricWriter) {
        this.entityManager = entityManager;
        this.metricWriter = metricWriter;
    }

    /**
      Push tweet to repository
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
      Result - recovered Tweet
    */
    public void publishTweet(String publisher, String text) {
        checkPublisher(publisher);
        checkTweet(text);

        this.metricWriter.increment(new Delta<Number>("published-tweets", 1));

        final TweetDAO tweet = new TweetDAO();
        tweet.setTweet(text);
        tweet.setPublisher(publisher);

        this.entityManager.persist(tweet);
    }

    /**
      Recover tweet from repository
      Parameter - id - id of the Tweet to retrieve
      Result - retrieved Tweet
    */
    public TweetDAO getTweet(Long id) {
        checkId(id);

        this.metricWriter.increment(new Delta<Number>("times-queried-one-tweet", 1));
        return findTweet(id);
    }

    /**
      Recover tweet from repository
      Parameter - id - id of the Tweet to retrieve
      Result - retrieved Tweet
    */
    public List<TweetDAO> listAllTweets() {
        this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));

        TypedQuery<TweetDAO> query = entityManager.createQuery("SELECT t FROM Tweet t WHERE pre2015MigrationStatus<>99 ORDER BY created DESC", TweetDAO.class);
        return query.getResultList();
    }

    private TweetDAO findTweet(Long id) {
        return this.entityManager.find(TweetDAO.class, id);
    }

    private void checkPublisher(String publisher) {
        if (!StringUtils.hasLength(publisher)) {
            throw new IllegalArgumentException("Publisher must have length");
        }
    }

    private void checkTweet(String text) {
        if (!StringUtils.hasLength(text) || text.length() > 140) {
            throw new IllegalArgumentException("Tweet must not be greater than 140 characters");
        }
    }

    private void checkId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Tweet's id must not be null");
        }
    }
}
