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
 *
 * synchronized 和 Lock区别
 * 1 存在层面  sync是Java关键字，是JVM底层实现的（c++ monitorObject对象） Lock是Java实现的，是JUC包中的一个接口
 * 2 锁的状态  sync无法判断是否获得锁成功，Lock可以判断是否获取锁成功（isHeldByCurrentThread()）
 * 3 锁的获取  sync中，A线程获得锁，B线程等待。如果A阻塞，则B一直等待。在Lock中，如果A获取锁，B可能会尝试获取锁，获取不到才会等待
 * 4 锁的释放  sync中，代码执行完毕或者异常之后会自动释放。LOCK中，必须在finally中释放。
 * 5 锁的类型  sync中，可重入、不可中断、非公平锁  LOCK，可重入、可中断、可公平、可不公平（sync不可中断的意思是：A线程获取锁，B线程等待，线程中断不能停止B的等待）
 * 6 锁的性能  sync中，适用于竞争不激烈的场景。（经历过锁的优化之后，SYNC在竞争不激烈的情况下，性能优于Lock）. LOCK，适用于竞争激烈的场景
 *
 * 对象的内存布局：
 *  
 *
 *
 * */
public class SyncTest {

}
