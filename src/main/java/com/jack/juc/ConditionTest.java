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
