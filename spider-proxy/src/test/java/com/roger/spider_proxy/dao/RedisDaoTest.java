package com.roger.spider_proxy.dao;

import com.roger.spider_proxy.entity.Proxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    @Autowired
    private RedisDao redisDao;

    @Test
    public void isExists() {
//        Proxy proxy=new Proxy("127.0.0.1",8888);
//        Proxy proxy=new Proxy("192.168.0.6",1234);
        Proxy proxy=new Proxy("192.168.0.6",1111);

        System.out.println(redisDao.isExists(proxy));
    }

    @Test
    public void add() {
//        Proxy proxy=new Proxy("192.168.0.6",1234);
//        Proxy proxy=new Proxy("127.0.0.1",8888);
        Proxy proxy=new Proxy("138.32.133.15",3243);
        System.out.println(redisDao.add(proxy));
    }

    @Test
    public void random() {
        for (int i = 0; i < 5; i++) {
            System.out.println(redisDao.random());
        }
    }

    @Test
    public void max() {
        redisDao.max(redisDao.random());
    }

    @Test
    public void count() {
        System.out.println(redisDao.count());
    }

    @Test
    public void all() {
        Set<Proxy> set=redisDao.all();
        System.out.println(set);
    }

    @Test
    public void decrease() {
//        for (int i = 0; i < 10; i++) {
//            Proxy proxy=redisDao.random();
//            System.out.println(proxy);
//            redisDao.decrease(proxy);
//        }
        for (int i = 0; i < 1; i++) {
            Proxy proxy=new Proxy("127.0.0.1",8888);
            redisDao.decrease(proxy);
        }
    }
}