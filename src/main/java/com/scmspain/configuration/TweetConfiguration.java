package com.scmspain.configuration;

import com.scmspain.controller.GlobalExceptionHandler;
import com.scmspain.controller.TweetController;
import com.scmspain.controller.mapper.TweetMapper;
import com.scmspain.controller.validation.TweeterMessageValidator;
import com.scmspain.services.TweetService;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class TweetConfiguration {
    @Bean
    public TweetService getTweetService(EntityManager entityManager, MetricWriter metricWriter) {
        return new TweetService(entityManager, metricWriter);
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService, TweetMapper tweetMapper) {
        return new TweetController(tweetService, tweetMapper);
    }

    @Bean
    public TweetMapper getTweetMapper(){
        return new TweetMapper();
    }

    @Bean
    public GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
