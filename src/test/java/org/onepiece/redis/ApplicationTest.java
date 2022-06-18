package org.onepiece.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.onepiece.redis.component.Receiver;
import org.onepiece.redis.model.User;
import org.onepiece.redis.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringBootTest
@Slf4j
public class ApplicationTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource(name = "generalRedisTemplate")
    private RedisTemplate<String, User> redisTemplate;

    @Autowired
    private Receiver receiver;

    @Autowired
    private CacheService cacheService;

    @Test
    public void testSave() {
        stringRedisTemplate.opsForValue().set("manman", "shenmanying", 1, TimeUnit.MINUTES);
        assertEquals("shenmanying", stringRedisTemplate.opsForValue().get("manman"));
    }

    @Test
    public void testObjectSerializable() {
        User user = new User();
        user.setId(1);
        user.setUsername("shenmanying");
        user.setPassword("521");
        redisTemplate.opsForValue().set("user", user, 1, TimeUnit.MINUTES);
        User receive = redisTemplate.opsForValue().get("user");
        assertEquals(receive, user);
    }

    @Test
    public void testCacheManager() {
        User u1 = cacheService.getUserById(1);
        User u2 = cacheService.getUserById(1);
        assertEquals(u1, u2);
    }

    @Test
    public void testMessageQueue() throws InterruptedException {
        while (receiver.getCount() < 1) {
            log.info("Sending message...");
            redisTemplate.convertAndSend("chat", "I love you, manman !!!");
            Thread.sleep(1000);
        }
    }
}
