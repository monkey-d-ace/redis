package org.onepiece.redis.config;

import org.onepiece.redis.component.Receiver;
import org.onepiece.redis.model.Parent;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class AppConfig {
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("127.0.0.1", 6379));
//    }

    @Bean(name = "generalRedisTemplate")
    public RedisTemplate<String, ? extends Parent> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ? extends Parent> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }

    @Bean
    public CacheManager getCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext.<String, Object>newSerializationContext()
                .key(RedisSerializer.string())
                .value(RedisSerializer.json())
                .hashKey(RedisSerializer.string())
                .hashValue(RedisSerializer.json())
                .build();

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(3))
                .serializeKeysWith(serializationContext.getKeySerializationPair())
                .serializeValuesWith(serializationContext.getValueSerializationPair())
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .transactionAware()
                .build();
    }

    @Bean
    public Receiver getReceiver() {
        return new Receiver();
    }

    @Bean
    public MessageListenerAdapter getMessageListenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.addMessageListener(messageListenerAdapter, new PatternTopic("chat"));
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }
}
