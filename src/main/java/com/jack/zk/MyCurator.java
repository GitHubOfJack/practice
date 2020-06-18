package com.jack.zk;

import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author 马钊
 * @date 2020-06-18 11:25
 */
public class MyCurator {
    public static CuratorFramework doConnection() {
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        return CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
    }

    @SneakyThrows
    public static void doCreate() {
        CuratorFramework curatorFramework = doConnection();
        curatorFramework.start();
        curatorFramework.create().forPath("mytest", "what".getBytes());
    }

    public static void doCreateChild() {

    }

    public static void doDelete() {

    }

    public static void lock() {

    }

    public static void leader() {

    }
}
