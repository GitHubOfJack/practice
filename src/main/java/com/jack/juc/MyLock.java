package com.jack.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主要介绍java.util.concurrent.locks包下的类
 *      AbstractQueuedSynchronizer
 *
 * ​	Condition
 *
 * ​	Lock
 *          lock()  --一直等待锁
 *          tryLock() -- 尝试获取锁，如果获取到返回true，否则返回false
 *          tryLock(long, TimeUnit) -- 尝试在L时间内获取锁，如果获取到返回true，否则返回false
 *          unlock() -- 解锁，必须放在finally中，一次lock对应一次unlock
 *          newCondition() -- 返回一个Condition对象(synchronized与Object.wait\notify组合使用) LOCK与Condition.await\notify组合使用
 *
 * ​	LockSupport
 *
 * ​	ReentrantLock
 *
 * ​	ReadWriteLock
 *
 * ​	ReentrantReadWriteLock
 * */
public class MyLock {
    public static void main(String[] args) {
        lock();
    }

    public static void lock() {

        Object object = new Object();
        synchronized (object) {
            int i = 1;
        }

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Condition condition1 = lock.newCondition();


        /*for (int i=0; i<10; i++) {
            final int j = i;
            new Thread(() ->{
                lock.lock();
                try {
                    System.out.println(j);
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }, i+"").start();
        }*/

        /*for (int i=0; i<10; i++) {
            final int j = i;
            new Thread(() ->{
                if (lock.tryLock()) {
                    try {
                        System.out.println(j+"线程获取到锁");
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println(j+"未线程获取到锁");
                }

            }, i+"").start();
        }*/

        /*for (int i=0; i<10; i++) {
            final int j = i;
            new Thread(() ->{
                try {
                    if (lock.tryLock(6, TimeUnit.SECONDS)) {
                        try {
                            System.out.println(j+"线程获取到锁");
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        System.out.println(j+"未线程获取到锁");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, i+"").start();
        }*/
    }
}
