package com.roger.spider_proxy.dao;

import com.roger.spider_proxy.entity.Proxy;
import com.roger.spider_proxy.enums.ProxyScore;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RedisDao {
    private Logger LOGGER= LoggerFactory.getLogger(this.getClass());


    private JedisPool jedisPool;

    private final String KEY="proxy_sorted_set";

//    private RuntimeSchema<Proxy> schema=RuntimeSchema.createFrom(Proxy.class);

    public RedisDao(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }

    public boolean isExists(Proxy proxy){
        try(Jedis jedis=jedisPool.getResource()){
            String member=proxy.toString();
            long update=jedis.zrank(KEY,member);
            return true;
        }catch (Exception e){
            LOGGER.debug(e.getMessage()+"redis返回为null,不存在");
            return false;
        }
    }

    public boolean add(Proxy proxy){
        try(Jedis jedis=jedisPool.getResource()){
            if(!isExists(proxy)) {
                jedis.zadd(KEY, ProxyScore.INITIAL_SCORE.getScore(), proxy.toString());
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return false;
        }
        return true;
    }

    public Proxy random(){
        if (count()==0){
            return null;
        }
        Proxy proxy=null;
        try(Jedis jedis=jedisPool.getResource()){
            Set<String> proxies=jedis.zrangeByScore(KEY,ProxyScore.MAX_SCORE.getScore(),ProxyScore.MAX_SCORE.getScore());
            Optional<Proxy> optional=convert(proxies).stream().parallel().findAny();
            if(optional.isPresent()){
                proxy=optional.get();
            }else{
                proxies=jedis.zrevrange(KEY,0,20);
                optional=convert(proxies).stream().parallel().findAny();
                if(optional.isPresent()){
                    proxy=optional.get();
                }
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return proxy;
    }

    public Proxy max(Proxy proxy){
        try(Jedis jedis=jedisPool.getResource()){
            if(isExists(proxy)){
                jedis.zadd(KEY,ProxyScore.MAX_SCORE.getScore(),proxy.toString());
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return proxy;
    }
    public long count(){
        long count=0L;
        try(Jedis jedis=jedisPool.getResource()){
            count=jedis.zcard(KEY);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return count;
    }
    public Set<Proxy> all(){
        Set<Proxy> proxies=new HashSet<>();
        try(Jedis jedis=jedisPool.getResource()){
            Set<String> set=jedis.zrange(KEY,0,-1);
            for(String proxyStr: set){
                String[] s=proxyStr.split(":");
                proxies.add(new Proxy(s[0],Integer.parseInt(s[1])));
            }

        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return proxies;
    }

    public void decrease(Proxy proxy){
        try(Jedis jedis=jedisPool.getResource()){
            Double score=jedis.zscore(KEY,proxy.toString());
            if(score>ProxyScore.MIN_SCORE.getScore()+10){
                jedis.zadd(KEY,score-10,proxy.toString());
            }else{
                jedis.zrem(KEY,proxy.toString());
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }

    }

    public Set<Proxy> proxyPool(){
        if (count()==0){
            return null;
        }
        Set<String> proxies=null;
        try(Jedis jedis=jedisPool.getResource()){
            proxies=jedis.zrangeByScore(KEY,ProxyScore.MAX_SCORE.getScore(),ProxyScore.MAX_SCORE.getScore());
            if(proxies.size()<5) {
                proxies = jedis.zrevrange(KEY, 0, 20);

            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return convert(proxies);
    }

    private Set<Proxy> convert(Set<String> proxiesStr){
        Set<Proxy> proxies=new HashSet<>();
        for(String proxyStr: proxiesStr){
            String[] s=proxyStr.split(":");
            proxies.add(new Proxy(s[0],Integer.parseInt(s[1])));
        }
        return proxies;
    }



}
