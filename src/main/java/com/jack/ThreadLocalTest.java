package com.jack;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal<T>
 *
 * 典型的以空间换时间的多线程并发解决方案
 *
 * 原理：
 * 每个Thread中都有一个ThreadLocalMap属性threadLocals,该对象是一个KV结构对象
 * 其中key=ThreadLocal对象本身  Value=T对象，也就是你真正需要的对象
 * 注意，key是WeakReference
 * 此处可能会产生内存泄漏问题，原因
 * 如果线程是一个线程池中的核心线程（长时间存在），那么value这个值会长时间存在内存中，
 * 虽然这个值已经没有用了（因为KEY是弱引用，GC的时候已经回收，但是VALUE的指针会一直存在线程中）
 *
 * 最佳实践就是 调用了ThreadLocal#set()方法之后，需要调用ThreadLocal#remove()方法
 * */
public class ThreadLocalTest {
    public static void main(String[] args) {
        ThreadLocal<Person> tl = new ThreadLocal<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Person person = new Person();
                person.setAge(18);
                person.setName("zs");
                tl.set(person);
            }
        }, "first").start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(180);
                Person person = tl.get();
                System.out.println(person);
            }
        }).start();
    }
}
