package com.jack.jvm;

/**
 * 类变量初始过程
 * 成员变量初始化（new 执行init方法 栈指针指向可能会触发DCL问题）
 *
 * 给类变量赋值有两种方式：第一是显式赋值，第二是静态代码块赋值   这两种方式最终会变成clinit方法
 *
 * 给实例变量赋值有三种方式：第一种是显式赋值，第二种是代码块赋值，第三种是构造函数赋值 这三种方式最终会变成init方法
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
