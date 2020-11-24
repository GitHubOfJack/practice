package com.jack.jvm;

/**
 * 创建对象的方式：
 * 1 new (XXXBuilder\XXXFactory)
 * 2 反射：class.newInstance()，只能是public无参的构造方法,constructor.newInstance()
 * 3 clone(),深COPY和浅COPY
 * 4 反序列化（文件、网络）
 *
 * 对象创建的步骤：
 *
 * 例如:Object obj = new Object();
 *  底层字节码指令
 *  new #8    // class java/lang/Object
 *  dup
 *  invokespecial #1 //Method java/lang/Object."<init>":()V
 *  astore_1
 *  return
 *
 *  理解对象的半初始化（以及DCL问题）
 *  虚方法和非虚方法（编译期就确定下来的方法，如private方法、构造器、父类方法、静态方法、final方法）
 *  执行非虚方法:invokestatic invokespecial
 *  执行虚方法:invokevirtual invokeinterface
 *  invokedynamic执行lamda表达式的
 *
 * 1 判断对象的类信息是否加载，如果没有加载（加载、链接（验证、准备、解析）、初始化）
 * 2 为对象分配内存 new
 *      此处需要考虑两个问题
 *          1 采用什么方式分配内存：指针碰撞（内存连续）和空闲列表（内存不连续CMS）
 *          2 处理并发问题：CAS重试，TLAB
 * 3 初始化内存空间-实例变量赋0值
 * 4 设置对象头信息（是否开启了偏向锁、开启了指针压缩、开启了oops压缩）
 * 5 执行方法的init方法
 *
 * 对象访问的方式：
 * 1 句柄池
 * 2 直接指针
 *
 * 垃圾收集：
 * 1 垃圾收集算法：
 *      1 什么是垃圾：不再使用的对象
 *      2 如何标记垃圾对象：
 *          1 引用计数算法：优点，实现简单、效率高；缺点：需要记录引用次数，浪费空间和时间，无法解决循环引用问题
 *          2 根搜索算法：可以解决循环引用问题
 *              gc-roots有哪些：（非堆指向堆对象）
 *                  1 两个栈中的指针指向的对象
 *                  2 方法区中常量、方法区中的静态变量（1.7之后字符串常量池和静态变量移至堆中）
 *                  3 JVM所需要的对象（rt.jar中的类生成的对象）
 *                  4 锁对象
 *                  5 活动的线程对象
 *          3 对象标记阶段需要STW,同时为了提高效率维护了OOPMAP,OOPMAP只有到了safepoint才会更改
 *      3 如何回收垃圾对象：
 *          1 标记-清除算法：效率中等、内存碎片、空间要求小
 *          2 复制算法：效率高、内存整齐、空间要求大
 *          3 标记-整理算法：效率最低、内存整齐、空间要求小
 *          4 分代收集算法：
 *          5 增量收集算法：
 *          6 分区收集算法：
 * 2 垃圾收集器：
 *      1 serial
 *      2 parnew
 *      3 parallel scavenge
 *      4 serial old
 *      5 cms
 *      6 parallel old
 *      7 G1
 *      8 ZGC
 * 3 垃圾收集相关概念：
 *      oopMap
 *      card table
 *      Rset
 *      safepoint
 *      saferegion
 *
 * */
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class GCTest {
    private static final int _1M = 1024*1024;
    public static void main(String[] args) throws InterruptedException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        //doTest1();
        /*doTest2();
        System.gc();*/

        GCTest gcTest = GCTest.class.newInstance();
        System.out.println(gcTest);
        Constructor<GCTest> constructor = GCTest.class.getConstructor();
        GCTest gcTest1 = constructor.newInstance();
        System.out.println(gcTest1);
        /*Constructor constructor = new Constructor<GCTest>();
        constructor.newInstance();*/
    }

    public static void doTest1() {
        byte[] a1, a2, a3, a4;
        a1 = new byte[2*_1M];
        a2 = new byte[2*_1M];
        a3 = new byte[2*_1M];
        a4 = new byte[4*_1M];
    }

    public static void doTest2() throws InterruptedException {
        List<ByteObject> list = new ArrayList<>();
        for (int i=0; i<10000; i++) {
            ByteObject byteObject = new ByteObject();
            list.add(byteObject);
            Thread.sleep(1000);
        }
    }

    private static class ByteObject {
        private byte[] placeHolder = new byte[64*1024];
    }

    public void doTest() {
        Object object = new Object();
    }
}
