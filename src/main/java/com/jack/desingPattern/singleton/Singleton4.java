package com.jack.desingPattern.singleton;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 内部类单例
 *      关注点：
 *          1 构造方式是private
 *          2 有一个静态内部类并且是private
 *          3 内部类中有一个静态属性是单例对象，采用饿汉模式实现
 *      优点：
 *          1 外部类加载时并不需要立即加载内部类，内部类不被加载则不去初始化INSTANCE，故而不占内存
 *          2 JVM加载类会保证单例
 *      缺点：
 *          1 可以被反射获得
 *          2 序列化问题(要么就不实现Serializable方法，要么就会有序列化问题)
 *          3 传参很麻烦
 * */
public class Singleton4 implements Serializable {
    private static volatile boolean hasCreate = false;
    /**
     * 如果防治反序列化,可以在构造其中指定，但是还是无法避免反射情况的攻击
     * */
    private Singleton4(){
        if (hasCreate) {
            throw new RuntimeException("error");
        }
        hasCreate = true;
    }

    private static class SingletonHolder{
        private static final Singleton4 INSTANCE = new Singleton4();
    }

    public static Singleton4 getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private Object readResolve(){ return getInstance(); }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //正常的方式获取单例对象
        Singleton4 instance = Singleton4.getInstance();

        //写入磁盘
        FileOutputStream fos = new FileOutputStream("d:/single");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(instance);
        oos.close();
        fos.close();

        //从磁盘读取对象
        FileInputStream fis = new FileInputStream("d:/single");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Singleton4 innerSingle = (Singleton4) ois.readObject();

        System.out.println(instance);
        System.out.println(innerSingle);
        System.out.println(innerSingle == instance);
    }
}
