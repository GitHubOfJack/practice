package com.jack.juc;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList解析
 * final transient ReentrantLock lock = new ReentrantLock();
 * private transient volatile Object[] array;
 *
 * add代码
 * 流程梳理：首先获取锁，然后数组copy,最后添加元素,释放锁
 * public boolean add(E e) {
 *         final ReentrantLock lock = this.lock;
 *         lock.lock();
 *         try {
 *             Object[] elements = getArray();
 *             int len = elements.length;
 *             Object[] newElements = Arrays.copyOf(elements, len + 1);
 *             newElements[len] = e;
 *             setArray(newElements);
 *             return true;
 *         } finally {
 *             lock.unlock();
 *         }
 *     }
 *
 * get代码流程
 *  获取数组(此时的数组可能是原始数据，也可能是锁定之后扩容之后的数组)，获取数组元素
 *  注意，此时读取数据和添加数据可以同时进行，读取并没有加锁，只是不能保证读取的一定是最新的数据
 *  public E get(int index) {
 *         return get(getArray(), index);
 *     }
 *
 *  final Object[] getArray() {
 *         return array;
 *     }
 *
 * */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
        list.add("abc");
        String s = list.get(0);
        System.out.println(s);
        list.remove("abc");
    }
}
