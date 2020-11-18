package com.jack.juc;

import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger
 * 底层是CAS+volatile实现的
 *
 * CAS的缺点：
 * 1 ABA问题，改进方式   加版本号  具体实现AtomicStampedReference
 * 2 自旋时间过长导致消耗CPU资源,自适应自旋
 * 3 只能保证一个共享变量的原子操作，可以使用AtomicReference
 *
 *
 * 自旋锁使用场景：
 * 如果业务逻辑执行时间很短，没有必要执行线程的切换（需要用户态->核心态转变），如果采用线程自旋的形式，会减少获取锁的成本。
 *
 * 如果竞争很激烈或者业务逻辑执行时间很长，采用自旋的形式不合适
 * （参考sync锁升级过程,如果自旋的线程数=CPU核数/2，或者自旋次数超过10次就会升级为重量级锁）
 *
 *
 * */
public class CasTest {
    private static AtomicInteger ai = new AtomicInteger(0);

    private static CountDownLatch latch = new CountDownLatch(100000);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
            new Thread(() -> {
                ai.getAndIncrement();
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println(ai.get());
    }
}
