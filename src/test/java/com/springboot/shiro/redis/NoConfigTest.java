package com.springboot.shiro.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;



@SpringBootTest
@RunWith(SpringRunner.class)
public class NoConfigTest {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Test
    public void noConfigTest(){
        redisTemplate.opsForValue().set("name","张飞");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }

}
