package com.scmspain.controller.validation;

import com.scmspain.controller.TweetUtils;
import com.scmspain.controller.command.PublishTweetCommand;
import org.apache.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TweeterMessageValidator implements ConstraintValidator<ValidateTweet, String> {
    private static final Logger logger = Logger.getLogger(TweeterMessageValidator.class);

    @Override
    public void initialize(ValidateTweet constraintAnnotation) {
        logger.info("Initialized TweeterMessageValidator");
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final String message = new TweetUtils().getMessageWithoutLinks(value);
        return message.length() <= 140;
    }
}
