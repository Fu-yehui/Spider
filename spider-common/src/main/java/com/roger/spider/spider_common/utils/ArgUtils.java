package com.roger.spider.spider_common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class ArgUtils {

    public static void notNull(final Object o){
        if(o == null){
            throw new NullPointerException(ObjectUtils.getSimpleName(o)+" can not be null");
        }
    }
    public static void notEmpty(final String str ,String name){
        if(str == null){
            throw new NullPointerException(name +" can not be null");
        }
        if(str.length() == 0 ){
            throw new IllegalArgumentException(name+" can not be empty");
        }
    }

    public static boolean isEmpty(final Collection<?> collection ){
        return (collection==null || collection.isEmpty());
    }



    public static void check(final boolean expression,final String message){
        if(!expression){
            throw new IllegalArgumentException(message);
        }
    }
}
