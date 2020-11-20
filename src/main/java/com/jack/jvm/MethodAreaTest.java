package com.jack.jvm;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 逃逸分析：栈分配、锁消除、标量替换
 *
 * 方法区：类信息、常量、静态变量、JIT及时编译的代码
 *
 * 类信息：类基本信息、域信息、方法信息
 * 类基本信息：类的全限定名、父类的全限定名、接口的全限定名、类的修饰符
 * 域信息：名称、类型、修饰符
 * 方法信息：方法名称、方法返回值、方法参数列表、方法的修饰符、方法的字节码指令、异常表
 *
 * 常量信息：数据类型的常量池（字符串类型的常量池1.6之前在方法区，1.7之后迁移到堆中）
 *
 * 静态变量：类的静态变量是存在方法区中的（final类型的静态变量是常量，在编译期就已经确定下来）
 *
 * JIT及时编译的代码：热点代码的编译执行，性能提升的关键
 *
 * 常用参数
 * 1.7之前   -XX:PermSize -XX:MaxPermSize
 * 1.8之后   -XX:MetaspaceSize(默认大小21M) -XX:MaxMetaspaceSize(我们项目中的大小在130M左右)
 *
 * */
public class MethodAreaTest {
    public static void main(String[] args) throws InterruptedException {

        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            alloc();
        }
        long e = System.currentTimeMillis();
        System.out.println(e - b);
    }

    public static void alloc() {
        byte[] b = new byte[2];
        b[0] = 1;
    }

}
