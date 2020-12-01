package com.jack.jvm;

/**
 * 创建对象的方式：
 * 1 new (XXXBuilder\XXXFactory)
 * 2 反射：class.newInstance()，只能是public无参的构造方法,constructor.newInstance()
 * 3 clone(),深COPY和浅COPY
 * 4 反序列化（文件、网络）
 *
 * 对象创建的步骤：
 *
 * 例如:Object obj = new Object();
 *  底层字节码指令
 *  new #8    // class java/lang/Object
 *  dup
 *  invokespecial #1 //Method java/lang/Object."<init>":()V
 *  astore_1
 *  return
 *
 *  理解对象的半初始化（以及DCL问题）
 *  虚方法和非虚方法（编译期就确定下来的方法，如private方法、构造器、父类方法、静态方法、final方法）
 *  执行非虚方法:invokestatic invokespecial
 *  执行虚方法:invokevirtual invokeinterface
 *  invokedynamic执行lamda表达式的
 *
 * 1 判断对象的类信息是否加载，如果没有加载（加载、链接（验证、准备、解析）、初始化）
 * 2 为对象分配内存 new
 *      此处需要考虑两个问题
 *          1 采用什么方式分配内存：指针碰撞（内存连续）和空闲列表（内存不连续CMS）
 *          2 处理并发问题：CAS+失败重试，TLAB
 * 3 初始化内存空间-实例变量赋0值
 * 4 设置对象头信息（是否开启了偏向锁、开启了指针压缩、开启了oops压缩）
 * 5 执行方法的init方法
 *
 * 对象访问的方式：
 * 1 句柄池
 * 2 直接指针
 *
 * 垃圾收集：
 * 1 垃圾收集算法：
 *      1 什么是垃圾：不再使用的对象
 *      2 如何标记垃圾对象：
 *          1 引用计数算法：优点，实现简单、效率高；缺点：需要记录引用次数，浪费空间和时间，无法解决循环引用问题
 *          2 根搜索算法：可以解决循环引用问题
 *              gc-roots有哪些：（非堆指向堆对象）
 *                  1 两个栈中的指针指向的对象
 *                  2 方法区中常量、方法区中的静态变量（1.7之后字符串常量池和静态变量移至堆中）
 *                  3 JVM所需要的对象（rt.jar中的类生成的对象）
 *                  4 锁对象
 *                  5 活动的线程对象
 *          3 对象标记阶段需要STW,同时为了提高效率维护了OOPMAP,OOPMAP只有到了safepoint才会更改
 *      3 如何回收垃圾对象：
 *          1 标记-清除算法：效率中等、内存碎片、空间要求小
 *          2 复制算法：效率高、内存整齐、空间要求大
 *          3 标记-整理算法：效率最低、内存整齐、空间要求小
 *          4 分代收集算法：
 *          5 增量收集算法：
 *          6 分区收集算法：
 * 2 垃圾收集器：
 *      1 seria（GCDetails中的信息是：def new generation）和serial old（GCDetails中的信息是：tenured generation）
 *          -XX:+UseSerialGC
 *          适用于单核小内存的场景
 *          采用复制和标记-压缩算法
 *      2 parNew(par new generation)
 *          -XX:+UseParNewGC
 *          -XX:ParallelGCThreads
 *          serial的多线程版本,用于新生代,采用复制算法
 *      3 parallel scavenge(GCDetails中的信息是：PSYoungGen)和parallel old(GCDetails中的信息是：ParOldGen)
 *          -XX:+UseParallelGC
 *          -XX:ParallelGCThreads(默认是cup的核数)
 *          -XX:MaxGCPauseMillis
 *          -XX:GCTimeRatio
 *          -XX:+UseAdaptiveSizePolicy
 *          与parNew的区别
 *              1 吞吐量优先的垃圾收集器
 *              2 有自适应策略
 *      4 cms(GCDetails中的信息是：concurrent mark-sweep generation)
 *          -XX:+UseConcMarkSweepGC
 *          可以调整发生垃圾回收堆空间阈值
 *          -XX:UseCMSCompactAtFullCollection
 *          -XX:CMSFullGCsBeforeCompaction
 *          -XX:ConcGCThreads
 *
 *          用于老年代垃圾回收，采用标记-清除算法，会产生内存碎片，并发的垃圾回收器，
 *          不能等到内存满了之后再进行gc,，存在回收失败的情况（concurrent mode failure），需要使用serial old作为后备方式
 *          低延迟的垃圾收集器
 *
 *          1 初始标记(STW)
 *          2 并发标记
 *          3 最终标记(STW，采用增量更新算法-三色标记)
 *          4 并发清除
 *      7 G1(GCDetails中的信息是：garbage-first heap)
 *          -XX:UseG1GC
 *          -XX:G1HeapRegionSize(1M-32M,2的N次幂),Region数量在2048左右
 *          -XX:MaxGCPauseMillis
 *          把堆分成大小相同的N个Region,每个Region都可以是Eden,Survivor,old区，还有一个大对象区，只要超过Region一半都属于大对象。
 *          g1把region当成单次回收的最小单元，每次回收都是region的整数倍，G1在后台维护一个优先级列表，根据设置的最大停顿时间，默认200毫秒，
 *          优先处理回收价值最大的Region
 *
 *          半衰平均值-最近的参考价值越大
 *
 *          新生代的占比是从5%-60%
 *
 *          缺点是：需要的内存空间更大(RSet需要占用很大的内存空间)，在小内存上性能可能还不如cms（均衡点在6-8G之间）
 *
 *          YGC（整个过程STW）：
 *              1 扫描GC-ROOTS（包含RSet中的数据）
 *              2 读取dirty card queue中的数据，更新RSet(dirty card queue是每个赋值操作都会通过写屏障插入记录,每个Region都会读取queue中的数据更新RSet)
 *              3 复制算法收集内存(Collection set就是待回收的Region集合，空闲的Region会被放入到linked list中供JVM使用)
 *          Mixed GC(YGC和部分old区Region的回收)---当内存使用默认超过45%的时候触发并发标记然后执行Mixed GC,Mixed GC默认会进行8次，
 *              即每次Mixed GC只会回收并发标记的1/8的老年代Region,但是默认当内存使用情况小于10%的时候就结束Mixed GC，每个Region只有垃圾超过65%才会进行回收
 *              1 初始标记(STW,会触发一次YGC)
 *              2 并发标记
 *              3 重新标记（STW，采用原始快照的方式-三色标记）
 *              4 筛选回收(STW，通过上面的流程已经能筛选出性价比最高的Region，然后设定的最大停顿时间，进行相应回收)
 *
 *
 *      8 ZGC
 *
 *      组合方式：
 *      1 serial->serial old
 *      2 parNew->serial old
 *      3 parallel scavenge -> serial old
 *      4 serial->cms
 *      5 parNew->cms->serial old
 *      6 parallel scavenge -> parallel old（1.7 1.8默认垃圾收集器）
 *      7 g1(1.9默认垃圾收集器)
 * 3 垃圾收集相关概念：
 *      oopMap
 *          栈中的指针不全都是指向堆中对象的，找出栈中的指向堆中对象的指针，把他们记录到oopMap中，进行minor gc时，只需要扫描这些oopMap对象就可以
 *          栈中的操作会改变oopMap,如果每次都去修改oopMap太耗性能，需要设置了safepoint，在安全点修改oopMap,如果安全点太少，线程不太容易执行到安全点，
 *          如果太多会频繁修改oopMap
 *      Rememberd set
 *          为了解决跨代引用，避免把整个老年代加入到GC-ROOTS中（包括各种部分区域收集的垃圾回收器，如g1,zgc等，都有相同问题），
 *          有三种精度的实现方案，字长精度，对象精度，卡精度（card table）
 *      card tablb(Remembered set的具体实现，一个"字节"数组):
 *          存在与新生代，用于记录老年代中的跨代引用（把老年代按照512byte的大小分成N多份，如果该部分任意一个对象存在跨代引用，
 *          就把相对应的card table的值变成1，minor gc进行时，只需要扫描相应的card table就可以）
 *          有了卡表之后需要考虑，什么时候更新，怎么更新卡表为dirty的问题？
 *          采用的是在执行赋值（包含跨区域赋值和非跨区域赋值）的机器指令的时候（主要是考虑jit即时编译的代码），执行写屏障（写环绕通知），（思考下读写屏障和内存屏的不同）
 *          每次引用更新都会产生额外的开销，不过这与minor gc时扫描整个老年代代价还是低。
 *          出了这个开销之外，还有一个缓存行的问题，卡表一个元素一个字节，64个卡表共享一个缓存行，如果不通线程更新位于64*512范围内的
 *          对象时，就会有问题。采用了先检查，只有当该卡表元素不是dirty时，才更新。
 *      safepoint和saferegion：
 *          当发生gc时，线程不能马上停止，必须在safepoint点才能停止（方法的调用、循环跳出、异常跳出）
 *              线程停止有两种形式：1 抢先式停止（线程先中断，如果没有到安全点，恢复继续跑到安全点停止） 2 主动式停止（设置一个标志，线程运行到安全点，查询标志）
 *          如果gc发生时，线程处于block状态（sleep）等时，不能等待线程跑到安全点，但是此时引用关系也不会发生变化，所以进入安全区域。
 *              线程阻塞时，线程标记进入安全区域，当gc发生时，会忽略进入安全区域的线程。但是等线程要出安全区域的时候，会先查看gc时候完成，如果没有完成，则等待gc完成后唤醒线程继续执行。
 *      强引用(死也不回收)：
 *          Object obj = new Object(); 导致oom的元凶
 *      软引用（内存不够就回收，可以通过引用获取对象）：
 *          Object obj = new Object();
 *          SoftReference softReference = new SoftReference<>(obj);
 *          obj = null;   必须释放obj引用
 *      弱引用（垃圾收集就回收，可以通过引用获取对象）
 *          WeakReference weakReference = new WeakReference<>(new Object());
 *      虚引用（不可以通过引用获取对象，必须和ReferenceQueue队列配合使用,垃圾回收发生时，会把对象放入queue中，进行相应处理）
 *          Object obj = new Object();
 *          ReferenceQueue q = null;
 *          PhantomReference phantomReference = new PhantomReference<Object>(object, obj);
 *      三色标记（用于标记阶段）：
 *          黑：表示已经被垃圾收集器访问过，且这个对象的所有引用（指向别的对象的引用）都已经扫描过。黑色对象表示它已经扫描过，并且是存活的。
 *          白：表示未被垃圾收集器访问过。可达性分析刚开始，所有对象都是白色的，分析阶段结束后，白色的代表不可达对象。
 *          灰：表示已经被垃圾收集器访问过，但这个对象上至少有一个引用还没扫描过。
 *          如果，用户线程stw，则没问题。
 *          如果，用户线程和垃圾回收线程并发执行，会出现两种问题。
 *              1 浮动垃圾。把原本垃圾对象标记为存活对象。
 *              2 存活对象标记为垃圾对象。
 *              为了解决问题2，有两种方案：
 *                  1 增量更新（黑色对象插入新引用）：cms采用，当黑色对象插入新的指针指向白色对象时，把这个关系记录下来，等并发扫描结束后，再将这些黑色对象为根再扫描一次。
 *                  2 原始快照（灰色对象删除新引用）：G1采用，当灰色对象要删除白色对象的引用关系时，把删除的引用关系记录下来，等并发扫描结束后，再将这些灰色对象扫描一次。
 *
 * */
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class GCTest {
    private static final int _1M = 1024*1024;
    public static void main(String[] args) throws InterruptedException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        //doTest1();
        /*doTest2();
        System.gc();*/

        GCTest gcTest = GCTest.class.newInstance();
        System.out.println(gcTest);
        Constructor<GCTest> constructor = GCTest.class.getConstructor();
        GCTest gcTest1 = constructor.newInstance();
        System.out.println(gcTest1);
        /*Constructor constructor = new Constructor<GCTest>();
        constructor.newInstance();*/
    }

    public static void doTest1() {
        byte[] a1, a2, a3, a4;
        a1 = new byte[2*_1M];
        a2 = new byte[2*_1M];
        a3 = new byte[2*_1M];
        a4 = new byte[4*_1M];
    }

    public static void doTest2() throws InterruptedException {
        List<ByteObject> list = new ArrayList<>();
        for (int i=0; i<10000; i++) {
            ByteObject byteObject = new ByteObject();
            list.add(byteObject);
            Thread.sleep(1000);
        }
    }

    private static class ByteObject {
        private byte[] placeHolder = new byte[64*1024];
    }

    public void doTest() {
        Object object = new Object();

        SoftReference softReference = new SoftReference<>(object);

        WeakReference weakReference = new WeakReference<>(new Object());

        ReferenceQueue q = null;
        PhantomReference phantomReference = new PhantomReference<Object>(object, q);
    }
}
