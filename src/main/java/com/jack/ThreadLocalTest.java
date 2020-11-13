package com.jack;
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
 * */
public class ThreadLocalTest {
}
