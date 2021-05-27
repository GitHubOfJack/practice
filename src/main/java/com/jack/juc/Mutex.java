package com.jack.juc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * AQS是抽象类，但是并没有抽象方法，因为如果是抽象类，所有抽象方法必须实现，但是一般情况下只需要实现部分方法。
 * AQS底层采用的是 （state+CLH+CAS) 实现的
 *
 * AQS类主要信息如下：
 *  属性信息如下：
 *      private transient volatile Node head;
 *      private transient volatile Node tail;
 *      private volatile int state;
 *      private transient Thread exclusiveOwnerThread;
 *  内部类信息如下：
 *      static final class Node {
 *          volatile int waitStatus;
 *          volatile Node prev;
 *          volatile Node next;
 *          volatile Thread thread;
 *          Node nextWaiter;
 *      }
 *
 *      public class ConditionObject implements Condition {
 *          private transient Node firstWaiter;
 *          private transient Node lastWaiter;
 *          await();
 *          signal();
 *          signalAll();
 *      }
 *  需要子类实现的方法如下：
 *      1 tryAcquire(int arg); 获取排它锁 成功则返回true，失败则返回false
 *      2 tryRelease(int arg); 释放排它锁 成功则返回true，失败则返回false
 *      3 tryAcquireShared(int arg); 获取共享锁  负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源
 *      4 tryReleaseShared(int arg); 释放共享锁 尝试释放资源，如果释放后允许唤醒后续等待结点返回true，否则返回false
 *      5 isHeldExclusively(); 是否线程独占  只有用到condition才需要去实现它
 *
 * 外部调用的时候使用的方法是(这些方法都是final的方法（模板方法模式）)：
 * AbstractQueuedSynchronizer.acquire(int arg);
 * AbstractQueuedSynchronizer.release();
 * AbstractQueuedSynchronizer.tryAcquireNanos();
 *
 * AbstractQueuedSynchronizer.acquireShared();
 * AbstractQueuedSynchronizer.releaseShared();
 * AbstractQueuedSynchronizer.tryAcquireSharedNanos();
 *
 *
 *
 *
 * CANCELLED(1)：表示当前结点已取消调度。当timeout或被中断（响应中断的情况下），会触发变更为此状态，进入该状态后的结点将不会再变化。
 * SIGNAL(-1)：表示后继结点在等待当前结点唤醒。后继结点入队时，会将前继结点的状态更新为SIGNAL。
 * CONDITION(-2)：表示结点等待在Condition上，当其他线程调用了Condition的signal()方法后，CONDITION状态的结点将从等待队列转移到同步队列中，等待获取同步锁。
 * PROPAGATE(-3)：共享模式下，前继结点不仅会唤醒其后继结点，同时也可能会唤醒后继的后继结点。
 * 0：新结点入队时的默认状态。
 * 注意，负值表示结点处于有效等待状态，而正值表示结点已被取消。所以源码中很多地方用>0、<0来判断结点的状态是否正常。
 *
 *
 * 需要说明的一点是：CLH队列中，head节点永远是一个哑巴节点，它不代表任何线程（即head节点中的thread永远是空），只有从次节点开始才代表了等待锁的线程。
 * 也就是说当线程没有抢到锁被包装成Node节点扔进队列时，即使队列时空的，它也会排在第二个。
 *
 * AQS中的CAS操作针对5个变量，AQS类中的head,tail,state和Node类中的waitStatus,next
 *
 * Lock接口：
 *  void lock();
 *  void lockInterruptibly() throws InterruptedException;
 *  boolean tryLock();
 *  boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
 *  void unlock();
 *  Condition newCondition();
 *
 *  ReentrantLock implements Lock
 *  主要内部类如下：
 *      abstract static class Sync extends AbstractQueuedSynchronizer {
 *          abstract void lock();
 *      }
 *      static final class NonfairSync extends Sync {
 *          final void lock() {
 *             if (compareAndSetState(0, 1))
 *                 setExclusiveOwnerThread(Thread.currentThread());
 *             else
 *                 acquire(1);
 *         }
 *      }
 *      static final class FairSync extends Sync {
 *          final void lock() {
 *             acquire(1);
 *         }
 *      }
 *  主要方法如下：
 *      1 构造器方法
 *          public ReentrantLock() {
 *              sync = new NonfairSync();
 *          }
 *      2 构造器方法
 *          public ReentrantLock(boolean fair) {
 *              sync = fair ? new FairSync() : new NonfairSync();
 *          }
 *      3 加锁方法
 *          public void lock() {
 *              sync.lock();
 *          }
 *      4 释放锁方法
 *          public void unlock() {
 *              sync.release(1);
 *          }
 *
 * 思考AQS,ReentrantLock,Lock关系
 *
 * ReentrantLock是独占锁，但是有公平独占锁和非公平独占锁两种类型
 * ReentrantReadWriteLock中的读锁是共享锁，也有公平共享锁和非公平共享锁两种类型
 * 需要仔细体会下独占锁和共享锁   公平锁和非公平锁的关系
 *
 * ReadWriteLock 没有继承 Lock接口
 *  主要方法：
 *      Lock readLock();
 *      Lock writeLock();
 *
 *
 * 之所以采用从后往前遍历，是因为我们处于多线程并发条件下，如果一个节点的next属性是null，并不能保证它是尾结点（
 * 可能新加入的尾节点还没来得及执行prev.next=node）但是如果一个队列能够入队，则它的prev属性一定是有值的，所以反向查找是最准确的
 *
 * 锁的释放必须在finally中
 *
 *
 * ReentrantReadWriteLock 实现了 ReadWriteLock
 *
 *
 *
 * 共享锁与独占锁的区别
 * 1 独占锁模式下，只有独占锁的节点释放了之后，才会唤醒后续节点的线程。
 * 2 共享锁模式下，当一个节点获取了共享锁，我们获取成功之后就可以唤醒后续节点线程，而不需要等待释放锁再唤醒线程。
 *  共享锁可以被多个线程同时持有，一个线程获取到锁，后续节点有很大几率可以获取到锁。所以，在获取锁和释放锁的时候都会唤醒后续节点的线程。
 *
 * Semaphore#tryAcquireShared方法的返回值是一个int类型(独占锁返回的是boolean类型-代表获取锁成功或者失败)
 * 0 代表获取锁成功，但是后续获取锁会失败
 * 大于0, 代表获取锁成功，后续获取锁大概率会成功
 * 小于0，代表获取锁失败
 *
 * 共享锁--在构造方法中指定了state的值（代表了可以有多少个线程同时获取锁）
 *
 * 在独占锁中new Node()中,nextWaiter指向Node.EXCLUSIVE=null
 * 在共享锁中new Node()中，nextWaiter指向Node.SHARED=new Node()--所有的节点的nextWaiter都指向同一个SHARED对象，可以用来判定下一个NODE是不是共享锁
 *
 * 在独占锁中setHead()--1 把head指针指向当前节点 2 当前节点的thread=null 3 当前节点的prev=null 4 元head节点的next=null(为了GC)
 * 在共享锁中setHeadAndPropagate() --1 setHead()(包含了以上所有动作) 2 如果state还有剩余锁&&下一个节点是共享节点，调用releaseShared()方法
 *
 * 释放锁的逻辑
 *
 *
 *
 *
 * 死锁：
 *  线程1持有a锁，尝试获取b锁
 *  线程2持有b锁，尝试获取a锁
 *
 * 解决死锁的方式：
 *  1 保持加锁的顺序是一样的，1和2两个线程都是按照先获取a再获取b
 *  2 设置锁超时时间
 *  3 死锁检测机制，例如通过map记录每个线程持有的锁，通过map就可以检测死锁，如果检测到死锁，释放相应的锁即可。
 *
 */

public class Mutex implements Lock, java.io.Serializable {

    /**
     * AQS源码分析
     * */
    public static void doLockTest() {

        /**
         * 构造函数中 指定默认使用非公平锁 sync = new NonfairSync();
         * */
        Lock lock = new ReentrantLock();
        /**
         * 调用NonfairSync的lock方法，底层调用aqs.acquire()方法
         * */
        lock.lock();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    System.out.println("子线程开始运行"+Thread.currentThread().getName());
                } finally {
                    lock.unlock();
                }
            }
        }, "lock子线程").start();

        try {
            //do buss
            TimeUnit.SECONDS.sleep(180);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        doLockTest();
        Lock lock = new ReentrantLock(true);
        lock.lock();
        try {
            //do buss
        } finally {
            lock.unlock();
        }

        Semaphore semaphore = new Semaphore(5);
        semaphore.acquire();
        semaphore.release();
    }

    // Our internal helper class
    private static class Sync extends AbstractQueuedSynchronizer {
        // Reports whether in locked state
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // Acquires the lock if state is zero
        public boolean tryAcquire(int acquires) {
            assert acquires == 1; // Otherwise unused
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // Releases the lock by setting state to zero
        protected boolean tryRelease(int releases) {
            assert releases == 1; // Otherwise unused
            if (getState() == 0) throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        // Provides a Condition
        Condition newCondition() {
            return new ConditionObject();
        }

        // Deserializes properly
        private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    // The sync object does all the hard work. We just forward to it.
    private final Sync sync = new Sync();

    public void lock() {
        sync.acquire(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }
}
