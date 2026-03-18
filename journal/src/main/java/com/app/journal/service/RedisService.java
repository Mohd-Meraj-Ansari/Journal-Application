package com.app.journal.service;

import com.app.journal.api.response.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            log.info("Redis GET called for key: {}", key);
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) {
                log.warn("Cache MISS for key: {}", key);
                return null;
            }
            log.info("Cache HIT for key: {}", key);
            return objectMapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            log.error("error ", e);
            return null;
        }
    }

    public void set(String key, Object o, Long ttl) {
        try {
            log.info("Redis SET called for key: {} with TTL: {}", key, ttl);
            String json = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, json, ttl, TimeUnit.SECONDS);
            log.info("Data stored successfully in Redis for key: {}", key);
        } catch (Exception e) {
            log.error("Error while saving to Redis for key: {}", key, e);
            log.error("error ", e);
        }
    }
}
