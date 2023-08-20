package com.sashika.demo.lunchbuddy;

import com.sashika.demo.lunchbuddy.cache.AdvancedCache;
import com.sashika.demo.lunchbuddy.cache.AppCache;
import com.sashika.demo.lunchbuddy.cache.SimpleCache;

import java.util.*;

public class InMemoryCache implements AppCache {
    Map<String, String> simpleMap = new HashMap<>();
    Map<String, Map<String, String>> fieldMap = new HashMap<>();

    Map<String, Set<String>> memberMap = new HashMap<>();
    @Override
    public long setFieldValue(String key, String field, String value) {
        Map<String, String> fields = fieldMap.get(key);
        if(fields == null){
            fields = new HashMap<>();
            fieldMap.put(key,fields);
        }
        fields.put(field,value);
        return 1;
    }

    @Override
    public Optional<String> getFieldValue(String key, String field) {
        Map<String, String> fields = fieldMap.get(key);
        if(fields == null){
            return Optional.ofNullable(null);
        } else {
            return Optional.ofNullable(fields.get(field));
        }
    }

    @Override
    public Map<String, String> getAllFields(String key) {
        return fieldMap.get(key);
    }

    @Override
    public long addToMembers(String key, String member) {
        Set<String> members = memberMap.get(key);
        if(members == null){
            members = new HashSet<>();
            memberMap.put(key,members);
        }
        members.add(member);
        return 1;
    }

    @Override
    public Set<String> getMembers(String key) {
        Set<String> members = memberMap.get(key);
        if(members == null) return Collections.EMPTY_SET;
        return members;
    }

    @Override
    public boolean isMember(String key, String member) {
        return getMembers(key).stream().anyMatch(m -> m.equalsIgnoreCase(member));
    }

    @Override
    public void putValue(String key, String value) {
        simpleMap.put(key, value);
    }

    @Override
    public String getValue(String key) {
        return simpleMap.get(key);
    }

    @Override
    public long removeKey(String key) {
        simpleMap.remove(key);
        fieldMap.remove(key);
        memberMap.remove(key);
        return 1;
    }
}
