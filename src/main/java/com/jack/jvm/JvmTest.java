package com.jack.jvm;

/**
 * 类变量初始过程
 * 成员变量初始化（new 执行init方法 栈指针指向可能会触发DCL问题）
 *
 * 给类变量赋值有两种方式：第一是显式赋值，第二是静态代码块赋值   这两种方式最终会变成clinit方法
 *
 * 给实例变量赋值有三种方式：第一种是显式赋值，第二种是代码块赋值，第三种是构造函数赋值 这三种方式最终会变成init方法
 *
 *
 *
 *
 * JVM指令总结
 * 1 JDK自带
 *      所有命令都支持 -help
 *      jps -m(main)l(location)v(virtual)
 *      jinfo [option] pid
 *          option如下：
 *          -flag <name>         to print the value of the named VM flag
 *          -flag [+|-]<name>    to enable or disable the named VM flag
 *          -flag <name>=<value> to set the named VM flag to the given value
 *          -flags               to print VM flags
 *          -sysprops            to print Java system properties
 *          <no option>          to print both of the above
 *          -h | -help           to print this help message
 *      jstat [option] pid [interval] [count] （实时查看虚拟机状态，可以查看各个区域大小）
 *          option如下：
 *          -class(加载或者卸载了多少个类，总大小是多少)
 *          -gc（GC信息，每个区域容量大小是多少，使用了多少）
 *     jstack [-l|-m] pid -l(lock) -m(mixed包含JVM栈和本地方法栈) 查看这个进程下所有的线程信息
 *          jstack 10 > /logs/catalina_out/jstack.dump可以导出到文件
 *          top 查看占用CPU最高的进程
 *          top -Hp pid查看pid进程中线程占用CPU的情况
 *          通过printf %x 172把10进制转换成16进制，可以查找nid=0x（xxx就是转换的结果）的线程信息
 *          jstack 进程ID|grep tid(16进制线程ID小写英文) -A60(前60行)
 *     jmap [option] pid （-heap实时查看虚拟机各个区域大小，可以查看所有加载的类信息-histo(-verbose:class\-XX:+TraceClassLoading)）
 *          option如下:
 *          -heap                查看各区域大小
 *          -histo[:live]        查看所有对象
 *          -clstats             查看class loader的统计信息
 *          -finalizerinfo       打印finalization队列的对象信息
 *          -dump:<dump-options> to dump java heap in hprof binary format
 *                          dump-options:
 *                            live         dump only live objects; if not specified,
 *                                         all objects in the heap are dumped.
 *                            format=b     binary format
 *                            file=<file>  dump heap to <file>
 *
 *  2 标准参数
 *      -version -verbose:[gc|class](可以打印GC信息和类加载信息 jmap -histo:live  -XX:+TraceClassLoading)
 *
 *  3 X参数
 *      -Xloggc:fileName -Xms -Xmx -Xmn -Xss
 *  4 XX参数
 *      参数查看相关：
 *          -XX:+PrintFlagsInitial -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags
 *      内存相关：
 *          -Xms -Xmx -Xmn -Xss -XX:NewRatio -XX:SurvivorRatio -XX:PermSize
 *          -XX:MateSpaceSize -XX:MaxPermSize -XX:MaxMetaSpaceSize -XX:MaxDirectMemorySize(默认与-Xmx相同)
 *      GC相关：
 *          GC日志：
 *              -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/home/GCEASY/gc.log
 *              -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=20M
 *              -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCApplicationStoppedTime
 *              -XX:+PrintTenuringDistribution
 *          垃圾收集器相关:
 *              serial
 *                  -XX:+UseSerialGC -XX:UseSerialOldGC
 *              parallel
 *                  -XX:+UseParallelGC -XX:+UseParalelOldGC -XX:ParallelGCThreads
 *                  -XX:MaxGCPauseMills -XX:GCTimeRatio -XX:+UseAdaptiveSizePolicy
 *              par new
 *                  -XX:+UseParNewGC -XX:ParallelGCThreads
 *              cms
 *                  -XX:UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction -XX:+UseCMSInitiatingOccupancyOnly
 *                  -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction -XX:ConcGCThreads
 *              g1
 *                  -XX:UseG1GC -XX:G1HeapRegionSize -XX:MaxGCPauseMillis -XX:InitiatingHeapOccupancyPercent
 *                  -XX:ParallelGCThreads -XX:ConcGCThreads -XX:G1MixedGCCountTarget -XX:G1HeapWastePercent
 *                  -XX:G1OldCSetRegionThresholdPercent -XX:G1MixedGCLiveThresholdPercent
 *          其它:
 *              -XX:+UseTLAB -XX:MaxTenuringThreshold -XX:PretenureSizeThreshold -XX:+DoEscapeAnalysis
 *              -XX:+EliminateAllocations -XX:+HandlePromotionFailure -XX:+HeapDumpOnOutOfMemoryError
 *              -XX:+TraceClassLoading
 * */
public class JvmTest {
    /**
     * 常量，编译期就已经确定下来了
     * 如果是static final 必须有默认值
     * 思考？是否可以对static final常量重新赋值？静态代码块？-不可以，编译期就已经确定了
     * */
    public static final int i = 0;

    /**
     * 类变量可以多次赋值
     * */
    public static int j = 1;

    static {
        j = 10;
    }

    /**
     * 成员变量可以多次赋值
     * */
    private int m = 1;

    {
        m = 2;
    }

    public JvmTest(int m) {
        this.m = m;
    }

    public static void main(String[] args) {

    }
}
