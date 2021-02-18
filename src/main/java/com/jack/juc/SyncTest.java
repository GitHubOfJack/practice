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
 * 2 锁的状态  sync无法判断是否获得锁成功，Lock的排他锁，可以判断是否获取锁成功（isHeldByCurrentThread()）
 * 3 锁的获取  sync中，A线程获得锁，B线程等待。如果A阻塞，则B一直等待。在Lock中，如果A获取锁，B可能会尝试获取锁，获取不到才会等待
 * 4 锁的释放  sync中，代码执行完毕或者异常之后会自动释放。LOCK中，必须在finally中释放。
 * 5 锁的类型  sync中，可重入、不可中断、非公平锁  LOCK，可重入、可中断、可公平、可不公平（sync不可中断的意思是：A线程获取锁，B线程等待，线程中断不能停止B的等待）
 * 6 锁的性能  sync中，适用于竞争不激烈的场景。（经历过锁的优化之后，SYNC在竞争不激烈的情况下，性能优于Lock）. LOCK，适用于竞争激烈的场景
 *
 * 对象的内存布局：
 *  64位操作系统中：
 *      对象头
 *          8个字节的MarkWord（jvm启动时，采用了延迟开启偏向锁的策略，因为在jvm加载类时，明确知道有锁的竞争.）
 *              无锁         hashcode（跟实际内存位置有关） 4位的年龄信息 所以最大是16  1位的0 表明无偏向  01表明是无锁(其实001才是表明无锁)
 *              偏向锁       ThreadId(偏向的线程ID)       4位的年龄信息 所以最大是16  1位的1 表明偏向锁  01表明是偏向锁（采用cas实现,UseBiasedLocking是否开启偏向锁）
 *                  如果UseBiasedLocking=false，则直接获取轻量级锁
 *                      如果ubl=true,则检查偏向锁标志是否是1，锁标志是否是01，如果不是该是什么锁，就尝试获取什么锁
 *                          如果是101，查看threadId是否是空，
 *                              如果是空，尝试cas更改ThreadId
 *                              如果非空，比较是否是当前线程
 *                                  如果是当前线程，获取锁成功。
 *                                  如果不是，则进行偏向锁的撤销。
 *                                      偏向锁撤销，等待全局安全点时，查看拥有锁的线程是否退出了同步代码块
 *                                          如果退出了，则设置成001
 *                                          如果没有退出，则升级为轻量级锁
 *              轻量级锁     指向线程栈中的LockRecord的指针                                          00表明是轻量级锁（采用cas实现）
 *                  LockRecord中有header和owner两个结构
 *                  当前对象是否有锁
 *                      如果没锁，将当前对象的MarkWord写入当前线程的栈帧的LockRecord的header中（可能有多个线程写入同样的MarkWord）
 *                      cas操作把获取到锁的线程的LockRecord指针写入MarkWord中（可能有多个线程写入，但是只有一个会成功）
 *                          如果写入成功将LockRecord中的owner指向对象的MarkWord，然后执行同步代码块，执行完同步代码块，采用cas操作把记录的原来的MarkWord值写回，然后释放锁。
 *              重量级锁     指向互斥量（重量级锁）的指针                                             10重量级锁
 *                  有两个队列，同步队列和等待队列，这个是需要用户态和内核态的转变的，所以比较重。
 *                      先判定owner是否为空
 *                          如果为空，则设置owner=当前线程,recursions=1,进入同步代码块
 *                          如果非空，查看owner是否是当前线程
 *                              如果是,recursions++,进入同步代码块
 *                              如果不是,自旋的方式等待，自旋失败，通过cas进入entryset中
 *          4个字节的KlassPointer（默认开启了指针压缩）
 *          如果是数据，还有4个字节的数组长度
 *      实例数据
 *          int 4个字节
 *          long 8个字节
 *          reference 4个字节（默认开启了指针压缩）
 *      对其填充
 *          对象的大小必须是8字节的倍数
 *
 *
 * 重入:
 *  对于不同级别的锁都有重入策略，偏向锁:单线程独占，重入只用检查threadId等于该线程；
 *  轻量级锁：重入将栈帧中lock record的header设置为null，重入退出，只用弹出栈帧，直到最后一个重入退出CAS写回数据释放锁；
 *  重量级锁：重入_recursions++，重入退出_recursions--，_recursions=0时释放锁
 *
 * */
public class SyncTest {

}
