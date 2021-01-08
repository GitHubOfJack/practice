package com.jack.juc;

import java.util.concurrent.ConcurrentHashMap;

/**
 *  1.7中的ConcurrentHashMap
 *  1.7中采用 Segment数组+HashEntry数组+链表实现
 *      Segment类继承了ReentrantLock,采用分段锁的形式
 *  查询或者插入数据时，要经过两次hash定位
 *  size方法(不能精确计算出当前的node大小)
 *  先采用不加锁的方式，连续计算元素的个数，最多计算3次：
 *      1、如果前后两次计算结果相同，则说明计算出来的元素个数是准确的；
 *      2、如果前后两次计算结果都不同，则给每个Segment进行加锁，再计算一次元素的个数；
 *
 *  1.8中的ConcurrentHashMap
 *
 *
 * */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("a", "a");
        map.get("a");
        map.size();
    }
}
