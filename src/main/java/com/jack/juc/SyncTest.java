package com.jack.juc;

/**
 * synchronized关键字
 *
 * 重量级锁ObjectMonitor
 * （1）owner，指向持有ObjectMonitor的线程；
 * （2）WaitSet，wait状态的线程队列，等待被唤醒，也就是调用了wait；
 * （3）EntrySet，等待锁的线程队列，
 * （4）Recursions,重入次数
 *
 * 同步流程
 * （1）有两个线程，线程A、线程B将竞争锁访问同步代码块，先进入ObjectMonitor的EntrySet中等待锁；
 * （2）当CPU调度线程A获取到锁则进入同步代码，ObjectMonitor owner属性指向线程A，线程B继续在EntryList中等待；
 * （3）线程A在同步代码中执行wait，则线程进入WaitSet并释放锁，ObjectMonitor owner属性清空；
 * （4）CPU调度使线程B获取到锁进入同步代码块，ObjectMonitor owner属性指向线程B，任务执行完退出同步代码之前调用notifyAll，线程A被唤醒，从WaitSet转到EntryList中等待锁，线程B退出同步代码块，ObjectMonitor owner属性清空；
 * （5）CPU调度使线程A获取同步锁，继续后续代码；
 *
 * 每一个JAVA对象都和一个ObjectMonitor对象相关联，关联关系存储在对象头中
 * 每一个试图进入代码块的线程都会被封装成ObjectWaiter对象，他们或者在EntryList中或者在WaitSet中等待称为ObjectMonitor的owner
 * */
public class SyncTest {

}
