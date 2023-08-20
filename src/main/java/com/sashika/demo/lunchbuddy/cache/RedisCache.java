package com.sashika.demo.lunchbuddy.cache;

import com.sashika.demo.lunchbuddy.config.AppProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPooled;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class RedisCache implements AppCache {
    AppProperties properties;
    JedisPooled jedis;

    public RedisCache(AppProperties properties) {
        this.properties = properties;
        this.jedis = new JedisPooled(properties.getCacheUrl());
    }

    @Override
    public void putValue(String key, String value) {
        jedis.set(key, value);
    }

    @Override
    public String getValue(String key) {
        return jedis.get(key);
    }

    @Override
    public long removeKey(String key) {
        return jedis.del(key);
    }

    @Override
    public long setFieldValue(String key, String field, String value) {
        return jedis.hset(key, field, value);
    }

    @Override
    public Optional<String> getFieldValue(String key, String field) {
        return Optional.ofNullable(jedis.hget(key, field));
    }

    @Override
    public Map<String, String> getAllFields(String key) {
        return jedis.hgetAll(key);
    }

    @Override
    public long addToMembers(String key, String member) {
        return jedis.sadd(key, member);
    }

    @Override
    public Set<String> getMembers(String key) {
        return jedis.smembers(key);
    }

    @Override
    public boolean isMember(String key, String member){
        return jedis.sismember(key, member);
    }
}
