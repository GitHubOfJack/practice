package com.jack.juc;

import java.util.concurrent.ConcurrentHashMap;

/**
 *  1.7中的ConcurrentHashMap
 *  1.7中采用 Segment数组+HashEntry数组+链表实现(Segment 数组长度为 16，不可以扩容，
 *  Segment[i] 的默认大小为 2，负载因子是 0.75，得出初始阈值为 1.5，也就是以后插入第一个元素不会触发扩容，插入第二个会进行第一次扩容
 *  只初始化了 segment[0]，其他位置仍然是 null)
 *      Segment类继承了ReentrantLock,采用分段锁的形式
 *      构造函数中初始化Segment数组和Segment[0]位置的HashEntry数组
 *  查询或者插入数据时，要经过两次hash定位
 *  put操作，先通过hash(key)定位出Segment位置，如果该位置Segment为空，使用Segment[0]处的数组长度和负载因子初始化，采用cas的方式设置Segment[i]的对象
 *  获取锁，然后再hash定位出HashEntry的位置，剩余操作同HashMap操作。
 *  size方法
 *  先采用不加锁的方式，连续计算元素的个数，最多计算3次：
 *      1、如果前后两次计算结果相同，则说明计算出来的元素个数是准确的；
 *      2、如果前后两次计算结果都不同，则给每个Segment进行加锁，再计算一次元素的个数；
 *
 *  1.8中的ConcurrentHashMap
 *      1.8中采用 Node数组+链表+红黑树实现
 *      第一次put操作才进行数组的初始化操作（懒加载模式）
 *      put操作:1 计算hash(key) 2 for(;;)死循环
 *      死循环内容如下：
 *          如果Node数组为空，则初始化Node数组
 *          如果table[i]为空，则创建一个Node节点，采用cas操作赋值
 *          如果table[i]不为空，则synchronized(table[i]节点)，循环遍历赋值或者替换（考虑红黑树等情况）。
 *      size方法：for(CountCell[]数组)相加，采用CountCell[]数组实现，底层是cas实现,put方法也会给CountCell[i]+1;
 *
 *
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
