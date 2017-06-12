package com.scmspain.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TweetUtils {
    final String patternStr = "https?:\\/\\/([\\da-zA-Z\\.-]+)\\.([a-zA-Z\\.]{2,6})([\\/a-zA-Z\\.-]*)*\\/?\\s";
    final Pattern pattern = Pattern.compile(patternStr);

    public List<String> getLinks(String message) {
        final List<String> links = new ArrayList<>();
        final Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            links.add(matcher.group().trim());
        }
        return links;
    }

    public String getMessageWithoutLinks(String message) {
        final StringBuilder sb = new StringBuilder();
        final Matcher matcher = pattern.matcher(message);

        int nextStart = 0;
        while (matcher.find()) {
            sb.append(message.substring(nextStart, matcher.start()));
            nextStart = matcher.end();
        }
        sb.append(message.substring(nextStart));

        return sb.toString();
    }
}