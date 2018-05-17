package com.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class SpringDataRedisListTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 右压栈：后添加的对象排在后边
     */
    @Test
    public void testList01(){
        redisTemplate.boundListOps("namelist1").rightPush("李白");
        redisTemplate.boundListOps("namelist1").rightPush("杜甫");
        redisTemplate.boundListOps("namelist1").rightPush("白居易");
        List namelist1 = redisTemplate.boundListOps("namelist1").range(0, 11);
        System.out.println(namelist1);
    }

    /**
     * 左压栈：后添加的对象排在前边
     */
    @Test
    public void testSetValue2(){
        redisTemplate.boundListOps("namelist2").leftPush("刘备");
        redisTemplate.boundListOps("namelist2").leftPush("关羽");
        redisTemplate.boundListOps("namelist2").leftPush("张飞");

        List namelist2 = redisTemplate.boundListOps("namelist2").range(0, 11);
        System.out.println(namelist2);
    }

    /**
     * 查询集合某个元素
     */
    @Test
    public void testSearchByIndex(){
        String s = (String) redisTemplate.boundListOps("namelist1").index(1);
        System.out.println(s);
    }

    /**
     * 移除集合某个元素
     */
    @Test
    public void testRemoveByIndex(){
        redisTemplate.boundListOps("namelist2").remove(1, "关羽");
    }

    @Test
    public void testDelete(){
        redisTemplate.delete("namelist2");
    }
}
