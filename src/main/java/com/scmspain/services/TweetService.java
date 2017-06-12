package com.scmspain.services;

import com.scmspain.dao.LinkDAO;
import com.scmspain.dao.TweetDAO;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    public void publishTweet(String publisher, String text, List<String> links) {
        checkPublisher(publisher);
        checkTweet(text);

        this.metricWriter.increment(new Delta<Number>("published-tweets", 1));

        final TweetDAO tweetDAO = new TweetDAO();
        tweetDAO.setTweet(text);
        tweetDAO.setPublisher(publisher);

        List<LinkDAO> linkDAOS = new ArrayList<>(links.size());
        LinkDAO linkDAO;
        for (String link : links) {
            linkDAO = new LinkDAO();
            linkDAO.setUrl(link);
            linkDAO.setTweet(tweetDAO);

            linkDAOS.add(linkDAO);
        }
        tweetDAO.setLinks(linkDAOS);

        this.entityManager.persist(tweetDAO);
    }

    /**
     Discard tweet to repository
     Parameter - id - Tweet's id to discard
     Result - discarded Tweet
     */
    public void discardTweet(Long id) {
        checkId(id);

        this.metricWriter.increment(new Delta<Number>("discarded-tweets", 1));

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<TweetDAO> criteriaUpdate = builder.createCriteriaUpdate(TweetDAO.class);
        Root<TweetDAO> root = criteriaUpdate.from(TweetDAO.class);

        Predicate condition = builder.equal(root.get("id"), id);
        criteriaUpdate.where(condition);

        criteriaUpdate.set(root.get("discarded"), true);
        criteriaUpdate.set(root.get("discardedDate"), new Date());

        Query query =  this.entityManager.createQuery(criteriaUpdate);
        query.executeUpdate();
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

        TypedQuery<TweetDAO> query = entityManager.createQuery("SELECT t FROM Tweet t WHERE pre2015MigrationStatus<>99 AND discarded = false ORDER BY created DESC", TweetDAO.class);
        return query.getResultList();
    }

    /**
     Recover discarded tweet from repository
     Parameter - id - id of the Tweet to retrieve
     Result - retrieved discarded Tweet
     */
    public List<TweetDAO> listAllDiscardedTweets() {
        this.metricWriter.increment(new Delta<Number>("times-discarded-tweets", 1));

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TweetDAO> criteriaSelect = builder.createQuery(TweetDAO.class);
        Root<TweetDAO> root = criteriaSelect.from(TweetDAO.class);

        Predicate discardedTrue = builder.equal(root.get("discarded"), true);
        Predicate notPre2015MigrationStatus = builder.notEqual(root.get("pre2015MigrationStatus"), 99);
        criteriaSelect.where(builder.and(discardedTrue, notPre2015MigrationStatus));

        Order discardedDate = builder.desc(root.get("discardedDate"));
        criteriaSelect.orderBy(discardedDate);

        TypedQuery<TweetDAO> query = entityManager.createQuery(criteriaSelect);
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
