package com.roger.spider_proxy.controller;

import com.alibaba.fastjson.JSONObject;
import com.roger.spider.spider_common.model.Proxy;
import com.roger.spider_proxy.dao.RedisDao;
import com.roger.spider_proxy.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Controller
@RequestMapping("/")
public class ProxyController {

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value = "/random",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public Proxy random(){
        return redisDao.random();
    }

    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public Set<Proxy> all(){
        return redisDao.all();
    }

    @RequestMapping(value="/test" , method = RequestMethod.GET)
    @ResponseBody
    public Proxy test(){
        String url="http://localhost:8080/proxy/random";
        RestTemplate restTemplate = new RestTemplate();
        String str= restTemplate.getForObject(url,String.class);

        JSONObject jsonObject= JSONObject.parseObject(str);
        Proxy proxy= new Proxy(jsonObject.getString("ip"),jsonObject.getInteger("port"));
        System.out.println(proxy);
        return proxy;

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
