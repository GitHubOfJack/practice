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
 *
 *
 * javac Test.java   生成Test.class文件
 *
 * javap -c Test.class 查看源码
 * javap -verbose Test.class 查看详细源码
 *
 *
 *
 *
 * .class文件组成包含7个部分
 * 1 魔数与class文件版本
 * 2 常量池（字面量和符号引用）
 *      2.1 字面量：文本字符串、申明为final类型的常量值
 *      2.2 符号引用：1类或接口的全限定名 2方法的名称和描述符 3字段的名称和描述符
 * 3 访问标志
 * 4 类索引、父类索引、接口索引
 * 5 字段表
 * 6 方法表
 * 7 属性表
 * */
public class JpsPractice {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(Integer.MAX_VALUE);
    }
}
