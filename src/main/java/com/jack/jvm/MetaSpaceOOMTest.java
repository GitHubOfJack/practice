package com.jack.jvm;


/**
 *
 * 打印类加载情况:-verbose:class
 *
 * -XX:+TraceClassLoading
 *
 * -XX:+TraceClassUnLoading
 * */
public class MetaSpaceOOMTest {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(90000);
        System.out.println("-----------------");
        System.out.println(JvmTest.class);
    }
}
