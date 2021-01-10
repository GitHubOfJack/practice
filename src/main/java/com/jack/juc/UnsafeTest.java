package com.jack.juc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe类
 * 单例模式(饿汉模式)
 * 通过静态getUnsafe方法获取单例对象，而且调用类必须是引导类加载器加载的，否则会报错java.lang.SecurityException
 *
 * 如果想要使用，1 要么写的class被bootstrap-classloader加载 2 要么通过反射
 *
 * 相关操作：
 *  内存操作：allocateMemory、freeMemory（堆外内存回收，是通过虚引用实现的）
 *  cas操作：compareAndSwapInt、compareAndSwapObject
 *  线程调度: park、unPark(LockSupport)
 *  对象属性的操作：objectFieldOffset、getObject、putObject
 *
 *
 *
 */
public class UnsafeTest {

    public static void main(String[] args) {
        //Unsafe unsafe = Unsafe.getUnsafe();
        //unsafe.addressSize();


        Field field = null;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe =  (Unsafe) field.get(null);
            System.out.println(unsafe);
            System.out.println(unsafe.addressSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
