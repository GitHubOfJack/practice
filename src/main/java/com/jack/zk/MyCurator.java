package com.jack.zk;

import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        Stat stat = curatorFramework.checkExists().forPath("/mytest");
        if (null == stat) {
            curatorFramework.create().forPath("/mytest", "what".getBytes());
        }
    }

    @SneakyThrows
    public static void doCreateChild() {
        CuratorFramework curatorFramework = doConnection();
        curatorFramework.start();
        Stat stat = curatorFramework.checkExists().forPath("/mytest/mychiled");
        if (null == stat) {
            curatorFramework.create().creatingParentsIfNeeded().forPath("/mytest/mychiled", "child".getBytes());
        }
    }

    public static void doDelete() {

    }

    @SneakyThrows
    public static void doTransaction() {
        CuratorFramework curatorFramework = doConnection();
        curatorFramework.start();
        CuratorOp curatorOp = curatorFramework.transactionOp().create().forPath("/transaction1");
        CuratorOp curatorOp1 = curatorFramework.transactionOp().create().forPath("/transaction2");
        CuratorOp curatorOp2 = curatorFramework.transactionOp().create().forPath("/mytest1");
        curatorFramework.transaction().forOperations(curatorOp, curatorOp1, curatorOp2);
    }

    @SneakyThrows
    public static void lock() {
        CuratorFramework curatorFramework = doConnection();
        curatorFramework.start();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i=0; i<10; i++) {
            final int j = i;
            executorService.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/mylock");
                    if ( lock.acquire(10, TimeUnit.SECONDS) )
                    {
                        try
                        {
                            // do some work inside of the critical section here
                            System.out.println("我"+j+"获取到的ZK锁");
                        }
                        finally
                        {
                            lock.release();
                            System.out.println("我"+j+"释放了ZK锁");
                        }
                    }
                }
            });
        }
        executorService.shutdown();
    }

    public static void leader() {
        CuratorFramework curatorFramework = doConnection();
        curatorFramework.start();
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter()
        {
            public void takeLeadership(CuratorFramework client) throws Exception
            {
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership
            }
        };

        LeaderSelector selector = new LeaderSelector(curatorFramework, "/leader", listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
    }

    public static void main(String[] args) {
        //doCreate();
        //doCreateChild();
        //lock();
        //leader();
        doTransaction();
    }
}
