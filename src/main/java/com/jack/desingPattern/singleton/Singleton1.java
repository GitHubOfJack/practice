package com.jack.desingPattern.singleton;

/**
 * 首先我们要先了解下单例的四大原则：
 *
 * 1.构造私有
 *
 * 2.以静态方法或者枚举返回实例
 *
 * 3.确保实例只有一个，尤其是多线程环境
 *
 * 4.确保反序列换时不会重新构建对象
 *
 *
 * 饿汉模式
 *  关注点：
 *      1 构造方法：必须是private
 *      2 有一个private static final的属性，重点final
 *      3 提供一个静态方法
 * 缺点：即使用不到这个单例对象，也会生成占用内存空间
 * */
public class Singleton1 {
    private final static Singleton1 instance = new Singleton1();
    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        return instance;
    }
}
