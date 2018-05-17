package com.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/spring/applicationContext-redis.xml")
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public  void test01(){
       // redisTemplate.boundValueOps("name").set("itcats");
        System.out.println(redisTemplate.boundValueOps("name").get());
    }

    @Test
    public  void test02(){
       redisTemplate.delete("name");
    }

    /**
     * 存入值
     */

    @Test
    public void testValue(){
        redisTemplate.boundSetOps("nameset").add("许攸");
        redisTemplate.boundSetOps("nameset").add("荀彧");
        redisTemplate.boundSetOps("nameset").add("徐庶");

        Set members = redisTemplate.boundSetOps("nameset").members();
        System.out.println(members);
    }
    /**
     * 删除集合中的某一个值
     */
    @Test
    public void deleteValue(){
        redisTemplate.boundSetOps("nameset").remove("徐庶");
        Set members = redisTemplate.boundSetOps("nameset").members();
        System.out.println(members);

    }

    /**
     * 删除整个集合
     */
    @Test
    public void deleteAllValue(){
        redisTemplate.delete("nameset");
        Set members = redisTemplate.boundSetOps("nameset").members();
        System.out.println(members);

    }

}
