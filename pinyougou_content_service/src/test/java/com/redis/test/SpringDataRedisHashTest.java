package com.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/applicationContext-redis.xml")
public class SpringDataRedisHashTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSetValue(){
        redisTemplate.boundHashOps("namehash").put("a", "唐僧");
        redisTemplate.boundHashOps("namehash").put("b", "悟空");
        redisTemplate.boundHashOps("namehash").put("c", "八戒");
        redisTemplate.boundHashOps("namehash").put("d", "沙僧");
    }

    @Test
    public void testGetKeys(){
        Set s = redisTemplate.boundHashOps("namehash").keys();
        System.out.println(s);
    }

    @Test
    public void testGetValues(){
        List values = redisTemplate.boundHashOps("namehash").values();
        System.out.println(values);
    }

    @Test
    public void testGetValueByKey(){
        Object object = redisTemplate.boundHashOps("namehash").get("b");
        System.out.println(object);
    }

    @Test
    public void testRemoveValueByKey(){
        redisTemplate.boundHashOps("namehash").delete("c");
    }

    @Test
    public void testDelete(){
        redisTemplate.delete("namehash");
    }
}
