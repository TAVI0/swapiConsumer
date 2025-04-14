package com.marcos.starwarsapi.service.utiles;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public <T> void put(String key, T value) {
        cache.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = cache.get(key);
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    public void evict(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }
}