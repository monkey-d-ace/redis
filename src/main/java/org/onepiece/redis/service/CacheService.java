package org.onepiece.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.onepiece.redis.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static org.onepiece.redis.constant.Constant.getUserTable;

@Service
@Slf4j
public class CacheService {
    @Cacheable(cacheNames = "users")
    public User getUserById(int id) {
        log.info("load user {} from DB", id);
        return getUserTable().get(id);
    }
}
