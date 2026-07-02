package com.hacnation.fileattente.infrastructure.adapter.out.redis;

import com.hacnation.fileattente.domain.port.RedisQueuePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisQueueAdapter implements RedisQueuePort {

    private static final String QUEUE_POSITION_KEY = "queue:%s:position:%s";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisQueueAdapter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setPosition(String service, String patientId, Integer position) {
        String key = String.format(QUEUE_POSITION_KEY, service, patientId);
        redisTemplate.opsForValue().set(key, String.valueOf(position));
    }

    @Override
    public Integer getPosition(String service, String patientId) {
        String key = String.format(QUEUE_POSITION_KEY, service, patientId);
        String value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            return Integer.valueOf(value);
        }
        return null;
    }

    @Override
    public void removePosition(String service, String patientId) {
        String key = String.format(QUEUE_POSITION_KEY, service, patientId);
        redisTemplate.delete(key);
    }
}
