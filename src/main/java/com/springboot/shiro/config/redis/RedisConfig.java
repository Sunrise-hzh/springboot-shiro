package com.springboot.shiro.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        //新建RedisTemplate模板
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        //给该模板配置redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //Jackson2序列化对象
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //Spring 序列化对象
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //key采用string序列化的方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //hash的key也采用string的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //value序列化方式采用jackson2
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式也采用jackson2
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
