package com.travel.user.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionCacheService {

    private final RedisTemplate<String, Object> redis;

    public void storeSession(UUID userId, Object session) {
        redis.opsForValue()
                .set("user-session:" + userId, session);
    }
}
