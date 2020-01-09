package com.jack;

/**
 * String 是final的  为什么？
 * final关键字   如果是static变量 要么在声明时指定，要么在静态代码块中指定，如果是成员变量 要么在声明时指定，要么在构造方法中指定 final修饰类 final修饰方法 final修饰变量
 * 底层结构是什么?
 * +操作的原理
 * StringBuilder StringBuffer的区别
 * 字符串常量池、类的常量池、运行时常量池的关系
 * 字符串常量池在运行时常量池中（在1.7之前在运行时常量池中，在1.7之后在堆中）
 * intern()方法
 * 方法区是接口->永久区和元空间是实现
 * */
public class StringPractice {
    private static final Integer i;
    private static final boolean b;
    private final char c;
    private final Integer m;
    static {
        i = 10;
        b = false;
    }

    public StringPractice(Integer m, char c) {
        this.m = m;
        this.c = c;
    }
    public static void main(String[] args) {
        StringPractice sp = new StringPractice(10);
        sp.m = 1000;
        String s1 = "aaa";
        String s2 = new String("aaa");
        String s3 = s2.intern();
        String s4 = new String("aaa");
        System.out.println(s1 == s2);//false
        System.out.println(s1 == s3);//true
        System.out.println(s2 == s4);//false
        System.out.println(StringPractice.i);
    }

}
