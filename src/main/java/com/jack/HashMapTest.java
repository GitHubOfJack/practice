package com.jack;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 *
 * 链表：一种极端情况下的二叉树(为了解决查询问题，所以有了平衡二叉树，但是平衡二叉树在插入和删除数据时会进行大量的旋转（查询效率高），
 * 所以一般情况下采用红黑树（红黑树兼顾查询效率和插入、删除效率）)
 * AVL树  红黑树  B+树
 * AVL数：平衡二叉数(左右节点高度差不会超过1，所以查询效率很高（树的高度低）)
 * 红黑树（通过左旋、右旋、着色）：
 *  1 节点颜色，只有红和黑两种
 *  2 根节点、叶子节点都是黑色的
 *  3 从任意节点到任何后代的叶子节点的每条路径都包含相同数目的黑色节点
 *  4 没有两个相连的红色节点（一个红色节点不能有红色的父节点或者红色的子节点），但是可以有两个相连的黑节点。
 * B+树:
 *
 * HashMap线程不安全的表现&线程不安全的原因&改进方法
 * 线程安全：共享资源，在多线程竞争情况下，每次执行均可预测出固定结果。
 * 表现：1 多个线程读取，多个线程写入会报错ConcurrentModificationException（每次修改modCount++）
 *      2 多线程写入，会出现值覆盖的情况（一个线程执行到链表头赋值时，挂起，另一个线程执行完，此时上个线程继续执行，则出现值覆盖的情况）
 * 改进方法:
 *      1 HashTable 方法加了synchronized关键字,锁定的是整个table对象
 *      2 Collections.synchronizedMap 方法会生成一个Collections内部类SynchronizedMap,
 *          该内部类有两个属性Map<K,V> m, final Object      mutex;其中mutex就是锁对象,方法执行前需要锁定mutex.
 *      3 ConcurrentHashMap 分段锁
 *
 *
 * 1.7中死循环的过程分析
 * 头插法的关键代码
 * //从旧数组中遍历，先遍历数组，然后遍历数组中的某个位置上的链表
 * //先找出节点在数组中的位置，然后把新数组中i位置上的值赋值给变量，把变量赋值给数组中i的位置，最后把变量指向原来的下一个变量
 * void transfer(Entry[] newTable, boolean rehash) {
 *         int newCapacity = newTable.length;
 *         for (Entry<K,V> e : table) {
 *             while(null != e) {
 *                 Entry<K,V> next = e.next;
 *                 if (rehash) {
 *                     e.hash = null == e.key ? 0 : hash(e.key);
 *                 }
 *                 int i = indexFor(e.hash, newCapacity);
 *                 e.next = newTable[i];
 *                 newTable[i] = e;
 *                 e = next;
 *             }
 *         }
 *     }
 *
 * 1.8中HashMap
 *  有四个容量需要注意：1数组的大小 2数组被占用的大小 3链表的大小 4node节点的总个数
 *  常量：
 *      DEFAULT_INITIAL_CAPACITY = 1 << 4
 *          默认初始化数组大小--此处请注意是数组的大小
 *      DEFAULT_LOAD_FACTOR = 0.75f
 *          默认的加载因子--通过加载因子和数组容量，可以得出一个扩容的阈值。例如数组大小是16，
 *          加载因子是0.75,则扩容的阈值是16*0.75=12,意思是如果node的数量大于12则需要扩容.
 *          此处注意，hashmap中已经存储的node数量，并不是数组中占有的数量，如果所放位置为空，不会触发扩容，非空才会触发扩容
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
 *  int threshold--阈值,超过此值，就会引发resize.
 *      如果在构造函数中，表示数组大小，构造函数中如果指定了数组大小，最终会找到一个比指定值大的最小的2的N次幂的数值作为数组的最终大小,
 *      但是在第一次put是，此threshold会作为新的table的大小，然后重新计算阈值.
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
 *  hashMap.put方法流程
 *
 *  1 使用hash方法获取key值的hash值
 *  public V put(K key, V value) {
 *      return putVal(hash(key), key, value, false, true);
 *  }
 *
 *  2 首先通过key的hashCode方法获取到hash值，该值是int类型，注意key=null的hash值是0，即会放在数组中的第一个位置
 *    再通过扰动函数，获取到最终的hash值，扰动函数是高16位与低16位做异或运算，降低冲突的概率
 *    int类型的hashCode返回的是int本身
 *    String类型的hashCode返回的是s[0]*31^[n-1]
 *  static final int hash(Object key) {
 *      int h;
 *      return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
 *  }
 *
 *  3 put方法源码解析
 *  hash--通过hash方法返回的key的hash值
 *  onlyIfAbsent--是否使用新值覆盖旧址
 *
 *  梳理两个流程
 *      3.1 空的HashMap第一次put值
 *          发现数组为空->扩容->计算该节点在数组中的位置->放在此位置上结束
 *      3.2 HashMap-put值之后扩容
 *          发现数组不为空->
 *  final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
 *                    boolean evict) {
 *         Node<K,V>[] tab; Node<K,V> p; int n, i;
 *         //如果tab为空，表明是第一次PUT值，则进行扩容--懒加载的模式
 *         if ((tab = table) == null || (n = tab.length) == 0) {
 *             n = (tab = resize()).length;//返回的是新数组的大小
 *         }
 *         //采用(n - 1) & hash来确定该Node在数组中的位置
 *         if ((p = tab[i = (n - 1) & hash]) == null) {
 *              //如果该位置上没有值，则创建一个Node节点放入此位置
 *             tab[i] = newNode(hash, key, value, null);
 *         } else {
 *             //该位置上有值的逻辑--参考hasNode伪方法
 *         }
 *         ++modCount;
 *         //如果默认情况，第12次put对象并不会扩容，第13次才会扩容
 *         if (++size > threshold)
 *             resize();
 *         afterNodeInsertion(evict);
 *         return null;
 *     }
 *
 *      //进入此方法，意味着该位置上的链表不为空，至少有一个node
 *      hasNode()伪方法 {
 *             //p是table[i]上的node
 *             Node<K,V> e; K k;
 *             //如果p节点的hash值与新节点相同&&key相同（包含==与equals两种情况）
 *             if (p.hash == hash &&
 *                  ((k = p.key) == key || (key != null && key.equals(k)))) {
 *                 e = p;
 *              //如果节点属于TreeNode节点，即已经变成红黑树节点
 *              }  else if (p instanceof TreeNode) {
 *                 e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
 *              //链表的插入形式--1 循环遍历列表，如果key相同，则进行替换
 *              //如果key不相同，则看是否需要树化，如果不需要树化，则创建新节点，并放在队列的尾部(1.8是尾插法、1.7是头查法)
 *             } else {
 *                 for (int binCount = 0; ; ++binCount) {
 *                     if ((e = p.next) == null) {
 *                         p.next = newNode(hash, key, value, null);
 *                         //如果是链表中第9个元素，则开启树化操作--如果数组大小<MIN_TREEIFY_CAPACITY(64),不树化，而是扩容    
 *                         if (binCount >= TREEIFY_THRESHOLD - 1)
 *                             treeifyBin(tab, hash);
 *                         break;
 *                     }
 *                     if (e.hash == hash &&
 *                         ((k = e.key) == key || (key != null && key.equals(k))))
 *                         break;
 *                     p = e;
 *                 }
 *             }
 *             //如果发现了key相同的node,则使用旧值覆盖新值
 *             if (e != null) {
 *                 V oldValue = e.value;
 *                 if (!onlyIfAbsent || oldValue == null)
 *                     e.value = value;
 *                 afterNodeAccess(e);
 *                 return oldValue;
 *             }
 *         }
 *
 *         //treeifyBin中，如果数组大小<MIN_TREEIFY_CAPACITY(64),不树化，而是扩容
 *         treeifyBin()伪代码 {
*                 if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY) {
*                        resize();
*                 }
 *         }
 *
 *  4 扩容方法，返回的是新数组
 *  final Node<K,V>[] resize() {
 *         Node<K,V>[] oldTab = table;
 *         int oldCap = (oldTab == null) ? 0 : oldTab.length;
 *         int oldThr = threshold;
 *         int newCap, newThr = 0;
 *         //如果hashMap中原来有值，则扩容成原来的两倍，使用左移操作，此处如果就数组的大小大于等于16则阈值也扩容成原来的两倍，否则，阈值是用新容量*加载因子
 *         if (oldCap > 0) {
 *             if (oldCap >= MAXIMUM_CAPACITY) {
 *                 threshold = Integer.MAX_VALUE;
 *                 return oldTab;
 *             }
 *             else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
 *                      oldCap >= DEFAULT_INITIAL_CAPACITY)
 *                 newThr = oldThr << 1; // double threshold
 *         }
 *         //此处new HashMap(16)这类的构造函数生成的会走入此方法
 *         // initial capacity was placed in threshold
 *         else if (oldThr > 0)
 *             newCap = oldThr;
 *         //此处new HashMap()这类的构造函数生成的会走入此方法
 *         // zero initial threshold signifies using defaults 
 *         else {
 *             newCap = DEFAULT_INITIAL_CAPACITY;
 *             newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
 *         }
 *         //如果新阈值是0，则使用新容量*加载因子重新计算阈值
 *         if (newThr == 0) {
 *             float ft = (float)newCap * loadFactor;
 *             newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
 *                       (int)ft : Integer.MAX_VALUE);
 *         }
 *         threshold = newThr;
 *         //重新生成一个新的Node数组
 *         Node<K, V>[] newTab = (Node<K,V>[])new Node[newCap];
 *         table = newTab;
 *         //如果旧数组不为空，则使用循环遍历的方式，把原来节点上的节点放入新的数组中
 *         if (oldTab != null) {
 *             for (int j = 0; j < oldCap; ++j) {
 *                 Node<K,V> e;
 *                 if ((e = oldTab[j]) != null) {
 *                     oldTab[j] = null;
 *                     //如果链表上只有一个值，则重新计算在新数组中的位置，然后放入
 *                     if (e.next == null)
 *                         newTab[e.hash & (newCap - 1)] = e;
 *                     else if (e instanceof TreeNode)
 *                     //树化的操作
 *                         ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
 *                     else {
 *                         //低位的头节点和尾节点
 *                         Node<K,V> loHead = null, loTail = null;
 *                         //高位的头节点和尾节点
 *                         Node<K,V> hiHead = null, hiTail = null;
 *                         Node<K,V> next;
 *                         //循环遍历
 *                         do {
 *                             next = e.next;
 *                             //采用的是&操作，e.hash & oldCap  == 0 则放入低位，否则放入高位
 *                             if ((e.hash & oldCap) == 0) {
 *                                 if (loTail == null)
 *                                     loHead = e;
 *                                 else
 *                                     loTail.next = e;
 *                                 loTail = e;
 *                             }
 *                             else {
 *                                 if (hiTail == null)
 *                                     hiHead = e;
 *                                 else
 *                                     hiTail.next = e;
 *                                 hiTail = e;
 *                             }
 *                         } while ((e = next) != null);
 *                         //低位的放在原来的位置
 *                         if (loTail != null) {
 *                             loTail.next = null;
 *                             newTab[j] = loHead;
 *                         }
 *                         //高位的放在原来+oldCap的位置
 *                         if (hiTail != null) {
 *                             hiTail.next = null;
 *                             newTab[j + oldCap] = hiHead;
 *                         }
 *                     }
 *                 }
 *             }
 *         }
 *         return newTab;
 *     }
 */
public class HashMapTest {
    public static void main(String[] args) throws InterruptedException {
        HashMap map = new HashMap(3, 0.75f);
        map.put("a", "a");
        map.remove("a");

        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("a", "a");

        Collections.synchronizedMap(map);
    }
}
