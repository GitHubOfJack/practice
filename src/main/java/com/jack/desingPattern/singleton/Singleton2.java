package com.jack.desingPattern.singleton;

/**
 * 懒汉模式
 *      关注点：
 *          1 构造方法必须是private
 *          2 懒加载
 *      缺点 ：
 *          会有并发问题（对象的半初始化）
 *
 * */
public class Singleton2 {
    private static Singleton2 instance;
    private Singleton2() {

    }

    public static Singleton2 getInstance() {
        if (null == instance) {
            instance = new Singleton2();
        }
        return instance;
    }
}
