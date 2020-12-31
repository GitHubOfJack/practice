package com.jack;

/**
 * expungeStaleEntries expunge驱逐 stale陈旧的、腐朽的
 * 该方法是实现弱键回收的关键
 * 核心内容是：移除table中和referenceQueue并集的数据（referenceQueue中保存的是被gc的key,注意是key）
 * 效果是：key在gc的时候被回收，value是在key清除后访问WeakHashMap的时候被清除
 * */
public class WeakHashMapPractice {

}
