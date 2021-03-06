package com.jack.jvm;

/**
 * @author 马钊
 * @date 2020-11-19 15:03
 *
 * 堆空间分为：新生代和老年代
 * 新生代分为：一个Eden区，两个Survivor区
 *
 * 对象分配流程：
 * new一个对象：
 *  如果开启了逃逸分析：如果在栈范围内，首先尝试在栈上分配
 *  如果不能在栈上分配，首先尝试在Eden区的TLAB(Thread Local Allocation Buffer)尝试分配
 *  如果TLAB不能分配，则尝试在Eden区分配，如果Eden空间不足，则进行一次Minor GC，如果GC后还不能存放，则直接放在老年代。
 *  如果老年代也放不下，触发一次FULL GC， FULL GC之后仍不能放下，则OOM
 *  YGC中，会清空Eden和一个Survivor，然后把存活的对象复制到另一个Survivor中，如果对象的年龄大于阈值，直接进入老年代
 *
 *
 *
 * 优先分配Eden
 * 大对象直接进入老年代
 * 长期存活的对象进入老年代
 * 如果Survivor中相同年龄的所有对象的大小的总和大于Survivor空间的一般，年龄大于或者等于该年龄的对象直接进入老年代
 * 空间分配担（前提是在要进行Minor GC之（可以理解成eden区满的时候），如果老年代中的连续内存大于新生代的对象总大小或者历次晋升的平均大小就会进行Minor GC,否则进行Full GC）
 *
 *
 * 什么时候触发Minor GC: Eden满的时候触发，S0和S1并不会触发GC，但是GC的时候会清理Survivor
 * 什么时候触发Full GC: 老年代满的时候触发，方法区满的时候触发，Eden区满的时候，老年代的内存空间小与新生代的对象总大小和历次晋升平均大小进行FULL GC
 *
 * 所有对象都只能在堆上分配么？并不是，还可以在栈上分配
 * 堆上的所有对象都是共享的么？并不是，TLAB，线程私有的
 *
 *
 *
 *
 *
 * 指定堆大小
 * -Xms（默认情况下是内存的1/64）
 * -Xmx（默认情况下是内存的1/4） 生产情况下Xms和Xmx的值是一样的（思考下为什么？）
 * -Xmn（新生代大小）
 * -XX:NewRatio（老年代与新生代的比例，默认是2）
 * -XX:SurvivorRati（Eden与Survivor的比例，默认是8，但是本地测试一般是6，为什么？）
 * -XX:+UseTLAB(开启TLAB，默认情况下是eden的1%，可以通过参数设计TLAB的大小)
 * -XX:MaxTenuringThreshold(设置晋升到老年代的阈值，默认是15，最大也只能是15？思考为什么？)
 * -XX:HandlePromotionFailure（空间担保策略，默认是开启）
 * -XX:PrintFlagsInitial（打印参数初始值）
 * -XX:PrintFlagsFinal（打印参数最终值）
 * -XX:PrintGC        -Xlog: gc(打印GC基本信息)
 * -XX:PrintGCDetails -Xlog: gc*(打印GC详情信息)
 * -XX:PrintHeapAtGC  -Xlog: gc+heap=debug（查看GC前后方法区、堆的可用容量变化）
 * -XX:PrintGCApplicationConcurrentTime -XX:PrintGCApplicationStoppedTime -Xlog: safepoint(查看GC过程中用户并发时间和停顿时间)
 * -XX:PrintAdaptiveSizePolicy(自动设置堆空间各分代区域大小、收集目标等内容。从parallel收集器开始)
 * -XX:PrintTenuringDistribution -Xlog: gc+age=trace(收集过后，存活对象的年龄分布信息)
 * 生产GC日志配置
 * -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/home/GCEASY/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=20M
 *
 * -XX:+PrintGCDetails
 * -XX:+PrintGCDateStamps
 * -Xloggc:/home/GCEASY/gc-%t.log（当JVM重启以后，会生成新的日志文件，新的日志也不会覆盖老的日志，只需要在日志文件名中添加%t的后缀即可，%t会给文件名添加时间戳后缀，格式是YYYY-MM-DD_HH-MM-SS）
 *
 * -XX:+HeapDumpOnOutOfMemoryError
 *
 * -XX:TraceClassLoading
 *
 * -XX:PretenureSizeThreshold(大对象大小，只在serial\parnew垃圾收集器中起作用)
 *
 * java -XX:PrintFlagsFinal | more
 * java -XX:PrintFlagsFinal | wc -l
 *
 * jps
 * jinfo -flag flagName pid
 * jinfo -flags pid
 * jinfo -flag +flagName pid
 * jinfo -flag flagName=xx pid
 *
 * jstat
 * jstack
 * jmap
 */
public class HeapTest {
    public static void main(String[] args) {
        int[] iarray = new int[2];
        iarray[0] = 0;
        iarray[1] = 1;

        int j = iarray[10];
    }
}
