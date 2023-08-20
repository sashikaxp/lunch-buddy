package com.sashika.demo.lunchbuddy.cache;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface AdvancedCache {
    long setFieldValue(String key, String field, String value);
    Optional<String> getFieldValue(String key, String field);
    Map<String, String > getAllFields(String key);

    long addToMembers(String key, String member);
    Set<String> getMembers(String key);

    boolean isMember(String key, String member);
}
