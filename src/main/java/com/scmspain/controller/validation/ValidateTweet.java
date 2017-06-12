package com.scmspain.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TweeterMessageValidator.class)
public @interface ValidateTweet {
    String message() default "{tweet.length}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}