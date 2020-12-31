package com.jack.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
*
 * 1.7 HashMap Entry ConcurrentHashMap Segment HashEntry
 * 1.8 HashMap Node  ConcurrentHashMap Node
 * AQS Node
 *
 * synchronized 和 Lock区别
 * 1 存在层面  sync是Java关键字，是JVM底层实现的（c++ monitorObject对象） Lock是Java实现的，是JUC包中的一个接口
 * 2 锁的状态  sync无法判断是否获得锁成功，Lock可以判断是否获取锁成功（isHeldByCurrentThread()）
 * 3 锁的获取  sync中，A线程获得锁，B线程等待。如果A阻塞，则B一直等待。在Lock中，如果A获取锁，B可能会尝试获取锁，获取不到才会等待
 * 4 锁的释放  sync中，代码执行完毕或者异常之后会自动释放。LOCK中，必须在finally中释放。
 * 5 锁的类型  sync中，可重入、不可中断、非公平锁  LOCK，可重入、可中断、可公平、可不公平（sync不可中断的意思是：A线程获取锁，B线程等待，线程中断不能停止B的等待）
 * 6 锁的性能  sync中，适用于竞争不激烈的场景。（经历过锁的优化之后，SYNC在竞争不激烈的情况下，性能优于Lock）. LOCK，适用于竞争激烈的场景
 *
 * firstWaiter lastWaiter
 *
 * */
public class ConditionTest {

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[10];

    int putptr, takeptr, count;

    //生产者，往数组中写数据
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }
            items[putptr] = x;
            if (++putptr == items.length) {
                putptr = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    //消费者，从数组中取数据
    public Object get(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        condition1.await();
        condition1.signal();
        condition1.signalAll();
    }
}
