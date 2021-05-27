package com.jack.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * @author 马钊
 * @date 2021-05-03 18:20
 */
public class GuavaTest {
    public static void main(String[] args) throws Exception{
        LoadingCache<Object, Object> build = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                //.refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(new CacheLoader<Object, Object>() {

                    @Override
                    public Object load(Object key) throws Exception {
                        System.out.println("key="+key);
                        return key;
                    }
                });

        Object a = build.get("a");
        System.out.println(a);
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        a = build.get("a");
        System.out.println(a);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
