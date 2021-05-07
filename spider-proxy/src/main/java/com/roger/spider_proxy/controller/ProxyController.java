package com.roger.spider_proxy.controller;

import com.roger.spider_proxy.dao.RedisDao;
import com.roger.spider_proxy.entity.Proxy;
import com.roger.spider_proxy.schedule.Scheduler;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
@RequestMapping("/")
public class ProxyController {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private Scheduler scheduler;

    @RequestMapping("/random")
    @ResponseBody
    public Proxy random(){
        return redisDao.random();
    }

    @RequestMapping("/count")
    @ResponseBody
    public long count(){
        return redisDao.count();
    }

    @RequestMapping("/start")
    @ResponseBody
    public Set<Proxy> schedule(){
        scheduler.schedule();
        return redisDao.all();
    }

    @RequestMapping("/stop")
    @ResponseBody
    public Set<Proxy> stop(){
        scheduler.end();
        return redisDao.all();
    }

}
