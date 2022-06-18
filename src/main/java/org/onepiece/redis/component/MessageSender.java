package org.onepiece.redis.component;

import org.onepiece.redis.model.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@EnableScheduling
@Component
public class MessageSender {
    @Resource(name = "generalRedisTemplate")
    private RedisTemplate<String, Parent> redisTemplate;

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        redisTemplate.convertAndSend("chat", "I love you manman. " + new Date());
    }
}
