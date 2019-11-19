package com.jack;

/**
 * 查看java进程命令
 * (ps -ef|grep java)  &  (ls -l)  ->  jps -l
 *
 * jinfo -flags 线程号
 *
 * jinfo -flag 参数名称 线程号
 * 参数分为三种：
 * 标准参数   -version -help
 * X参数      -Xint(解释执行\interpreted) -Xcomp(编译执行) -Xmixed(混合模式)
 * XX参数     分为boolean参数和key-value参数
 *              boolean参数形式：   -XX:[+-]<name>    其中“+”表示开启参数，“-”表示禁用参数   -XX+UseConcMarkSweepGc
 *              key-value参数形式：  -XX:<key>=<value>   -XX:MaxGcPauseMillis=500
 * 常用参数名称：
 *
 *   -Xms  初始化heap大小，默认为内存的1/64   相当于 -XX:InitialHeapSize
 *   -Xmx  最大heap大小，默认为内存的1/4      相当于 -XX:MaxHeapSize        oom堆空间溢出
 *   -Xss  stack空间大小,默认1024k           相当于 -XX:ThreadStackSize   如果超出会导致stackOverFlowError  oom栈空间溢出
 *   -Xmn  设置年轻代大小                    相当于 -XX:MaxNewSize
 *   -XX:MaxPermSize   -XX:MetaspaceSize    JDK1.7方法区大小   JDK1.8元空间大小
 *   -XX:MaxTenuringThreshold=15            存活多少次进入老年代
 *   -XX:NewRatio=3                        设置年轻代和年老代的比值  默认是年轻代:年老代=1:3
 *   -XX:SurvivorRatio=8                   设置eden区与survivor区的比值    默认是 eden:from:to=8:1:1
 *
 *
 *
 *
 * 查看JVM参数另一种方法
 * java -XX:+PrintFlagsInitial                    本机的初始化参数
 * java -XX:+PrintFlagsFinal -version             查看JVM
 * java -XX:+PrintCommandLineFlags -version
 *
 *
 *
 * 查看GC情况
 * -XX:PrintGCDetails
 * */
public class JpsPractice {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(Integer.MAX_VALUE);
    }
}
