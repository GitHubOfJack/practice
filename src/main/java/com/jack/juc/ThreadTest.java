package com.jack.juc;

/**
 *
 * Thread类继承了Runnable接口
 * 构造器中主要有两个参数一个是Runnable接口，一个是名称 new Thread(Runnable runnable, String name)
 *
 * 线程状态
 * Thread.State.NEW
 * Thread.State.RUNNABLE
 * Thread.State.BLOCKED(waiting for a monitor lock, enter a synchronized block/method)
 * Thread.State.WAITING(Object.wait, Thread.join, LockSupport.park)
 * Thread.State.TIMED_WAITING(Thread.sleep, Object.wait with timeout, Thread.join with timeout, LockSupport.parkNanos, LockSupport.parkUntil)
 * Thread.State.TERMINATED
 *
 * java中线程中断的两种方法：
 * 1 设置或者清除中断标志（interrupt status）
 * Thread.currentThread().isInterrupted();实例方法   返回线程是否中断的标识,不会重置中断标志
 * Thread.interrupted();静态方法    返回线程是否中断的标识&重置中断标志
 * 2 抛出中断异常(interruptedException)
 * Thread.currentThread().interrupt();实例方法--  中断线程
 * 中断线程的意义是：给等待或者执行中的线程一个机会，可以终止等待或者中断正在执行的任务。但是仅仅是一个机会，并不会真的终止线程。
 *
 * Thread.sleep, Object.wait, Thread.join等方法在被打断之后，会获取到打断异常，在抛出异常之后（即捕获到异常之后）会清除当前线程的中断标志
 *
 * 所谓中断一个线程，并不是让线程停止运行。仅仅是将线程的中断标志设置为true，或者在某些情况下抛出异常。在被中断的线程的角度看，仅仅是自己的中断
 * 标志变成true，或者代码中抛出了异常而已。（至于用不用这个标志来做业务处理，或者处理不处理异常，全靠自己的业务实现。）
 *
 * 若线程被中断前，如果该线程处于非阻塞状态(未调用过wait,sleep,join方法)，那么该线程的中断状态将被设为true, 除此之外，不会发生任何事。
 * 若线程被中断前，该线程处于阻塞状态(调用了wait,sleep,join方法)，那么该线程将会立即从阻塞状态中退出，并抛出一个InterruptedException异常，同时，该线程的中断状态被设为false, 除此之外，不会发生任何事。
 *
 * */
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i=0; i<100; i++) {
                if (Thread.interrupted()) {
                    return;
                }
                System.out.println("t1正在执行"+i);
                /*try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }*/
            }
        });
        t1.start();
        Thread.sleep(1);
        t1.interrupt();
    }
}
