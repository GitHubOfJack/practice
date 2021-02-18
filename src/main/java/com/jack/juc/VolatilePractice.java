package com.jack.juc;

/**
 * volatile关键字，轻量级的线程同步工具，
 * jmm(java memory model) java内存模型(可以画出内存模型)，规定需要满足三个条件
 * 1 可见性
 * 2 原子性
 * 3 有序性
 * 缓存行（64个字节）
 *
 *
 * volatile满足两种，可见性和有序性，但是不满足原子性(比较经典的是i++问题)
 * sync和lock也可以满足可见性，是释放锁之前会把变量刷回主存中
 * 重排序：指令重排和编译重排
 * 有序性：一个变量是boolean一个变量是int的两个线程问题（ais-as if serial happens-before）
 * volatile写happens-before在volatile读-实现了可见性
 * volatile通过memory barrier实现有序性
 *
 *      lock作用于主内存的变量，把一个变量标记为一个线程独占
 *      unlock
 *
 * ​	主内存read>工作内存load>变量>use
 *
 * ​	assign>变量store>工作内存write>主内存
 *
 * ​	Object obj = new Object()底层字节码文件
 *
 * ​	new
 *
 * ​	dump
 *
 * ​	invokespecial
 *
 * ​	astore
 *
 * ​	return
 *
 *
 *
 * 内存屏障和读写屏障
 *
 * volatile关键字的底层实现是：汇编代码会生成一个lock add 0指令
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
