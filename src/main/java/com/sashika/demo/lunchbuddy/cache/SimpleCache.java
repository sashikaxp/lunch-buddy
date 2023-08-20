package com.sashika.demo.lunchbuddy.cache;

import java.util.Map;

public interface SimpleCache {
    void putValue(String key, String value);

    String getValue(String key);



}
