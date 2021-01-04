package com.jack;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 *
 *
 * 1.8中HashMap
 *  有四个容量需要注意：1数组的大小 2数组被占用的大小 3链表的大小 4node节点的总个数
 *  常量：
 *      DEFAULT_INITIAL_CAPACITY = 1 << 4
 *          默认初始化数组大小--此处请注意是数组的大小
 *      DEFAULT_LOAD_FACTOR = 0.75f
 *          默认的加载因子--通过加载因子和数组容量，可以得出一个扩容的阈值。例如数组大小是16，
 *          加载因子是0.75,则扩容的阈值是16*0.75=12,意思是如果node的数量大于12则需要扩容.此处注意，hashmap中已经存储的node数量，并不是数组中占有的数量
 *      TREEIFY_THRESHOLD = 8
 *      MIN_TREEIFY_CAPACITY = 64
 *          如果一个链表中的node个数超过8&数组的长度大于等于64，则把该链表变成红黑树（注意其他链表不会变成红黑树）
 *      UNTREEIFY_THRESHOLD = 6
 *          如果一个链表中的node个数小于6个，则把红黑树变成链表(不需要考虑数组长度，因为数组长度不会再变小了)
 *
 *  变量：
 *  Node<K,V>[] table--HashMap底层是数组+链表的结构
 *  int size--HashMap中Node节点的个数
 *  int modCount--HashMap被修改的次数，用户快速失败
 *  int threshold--阈值，如果HashMap中Node节点个数大于此值，则需要扩容
 *  float loadFactor--加载因子
 *
 *  构造方法-无参数
 *  public HashMap() {
 *      this.loadFactor = DEFAULT_LOAD_FACTOR;
 *  }
 *
 *  构造方法-有参数
 *  public HashMap(int initialCapacity) {
 *      this(initialCapacity, DEFAULT_LOAD_FACTOR);
 *  }
 *
 *  public HashMap(int initialCapacity, float loadFactor) {
 *      this.loadFactor = loadFactor;
 *      this.threshold = tableSizeFor(initialCapacity);
 *  }
 *
 *  //使用无符号右移和或运算，找出比当前值大的，最小的2的N次幂
 *  static final int tableSizeFor(int cap) {
 *         int n = cap - 1;
 *         n |= n >>> 1;
 *         n |= n >>> 2;
 *         n |= n >>> 4;
 *         n |= n >>> 8;
 *         n |= n >>> 16;
 *         return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
 *  }
 *
 */
public class HashMapTest {
    public static void main(String[] args) {
        HashMap map = new HashMap();
        map.put("a", "a");
        map.remove("a");

        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("a", "a");
    }
}
