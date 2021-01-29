package com.jack.desingPattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * DCL模式
 *      主要点:
 *          1 构造方法是private
 *          2 属性必须是volatile的
 *          3 双检查
 *      缺点 ：
 *          1 写法复杂
 *          2 可通过反射获取
 * */
public class Singleton3 {
    private static volatile Singleton3 instance;
    private Singleton3() {

    }

    public static Singleton3 getInstance() {
        if (null == instance) {
            synchronized (instance) {
                if (null == instance) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Singleton3> constructor = Singleton3.class.getDeclaredConstructor();
        Singleton3 singleton3 = constructor.newInstance();
        System.out.println(singleton3);
    }
}
