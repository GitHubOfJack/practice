package com.jack.juc;

/**
 * volatile关键字，轻量级的线程同步工具，
 * jmm(java memory model) java内存模型(可以画出内存模型)，规定需要满足三个条件
 * 1 可见性
 * 2 原子性
 * 3 有序性
 *
 *
 * volatile满足两种，可见性和有序性，但是不满足原子性
 * volatile之所以能满足这两种特性底层是通过memory barrier实现的。
 * memory barrier禁止重排序和强制读取主存数据
 *
 *
 * 内存屏障和读写屏障
 * */
public class VolatilePractice {
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        VolatilePractice practice = new VolatilePractice();

        Thread t1 = new Thread(() -> {
            while (!practice.isFlag()) {

            }
            System.out.println(Thread.currentThread().getName()+"接收到退出信息，退出");
        }, "t1");

        Thread t2 = new Thread(() -> {
            practice.setFlag(true);
            System.out.println(Thread.currentThread().getName()+"修改信号量");
        }, "t2");

        t1.start();
        Thread.sleep(1000);
        t2.start();
    }
}
