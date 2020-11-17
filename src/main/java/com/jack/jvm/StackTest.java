package com.jack.jvm;

/**
 * 主要是JVM Stack的相关知识
 *
 * 可设置的参数-Xss   stacksize
 *
 * 此空间不会发生GC，但是会OOM和StackOverFlowError
 *
 * 每个方法对应一个栈帧,进入一个方法对应着一个栈帧的入栈和出栈
 * 每个栈帧包含：局部变量表、操作数栈、动态链接和方法返回地址
 *
 * 局部变量表存在：基本数据类型、引用类型、ReturnAddress类型(在编译器大小就已经确定)
 * (这个是数组结构，里面存储的是数字类型的值，每个单元称为一个Slot，大小是4个字节，
 * 所以double和long需要占用两个空间，就是两个Slot,如果是实例方法（包含构造方法），
 * 局部变量表中的第一个位置是this对象（所以在这些方法中才可以使用this），静态方法没有this,
 * 还有一点是如果超过了作用域(在一个方法中定义代码块)，可能会涉及到slot重复利用的问题)
 *
 * 操作数栈: 作用是临时存储数据，只有两个操作：入栈和出栈(在编译器大小就已经确定)
 * 这个也是数组结构，只是当成栈使用（所以不能指定索引取值,只有入栈和出栈两个操作）
 *
 * 思考：JVM运行时，局部变量表&操作数栈&执行引擎是怎么配合执行的
 *
 * 动态链接
 *
 * 方法返回地址
 * */
public class StackTest {

    private static int i = 0;

    /**
     * 可以使用-XX:+PrintFlagsInitial  -XX:+PrintFlagsFinal查看所有属性值
     * -XX:+PrintGCDetails -XX:+PrintCommandLineFlags
     * -XX:+UseCompressedOops  -XX:+UseCompressedClassPointers
     * jps
     * jinfo [option] <pid>
     *      -flag <name>         to print the value of the named VM flag
     *      -flag [+|-]<name>    to enable or disable the named VM flag
     *      -flag <name>=<value> to set the named VM flag to the given value
     *      -flags               to print VM flags
     * jinfo -flag ThreadStackSize 9 打印9这个进程的-XX:ThreadStackSize的值
     * jinfo -flags 9 打印9这个进程的所有flags
     * jinfo -flag +PrintGCDetails 开启(关闭)GC打印
     * jinfo -flag ThreadStackSize=1024 设置变量的值
     *
     * 模拟实现StackOverFlowError
     * 此处可以设置-Xss大小(64位操作系统默认大小是1M)，来延迟StackOverFlowError
     * */
    public static void main(String[] args) {
        System.out.println(i++);
        main(args);
    }

    /**
     * static 无参 无返回值
     * */
    public static void test1(){
        int i = 0;
    }

    /**
     * static 有参 有返回值
     * */
    public static String test2(int i, int j) {
        return  i + j + "";
    }

    /**
     * 实例方法 无参方法 有返回值
     * */
    public int test3() {
        int i = 0;
        int j = 1;
        return i + j;
    }

    /**
     * 实例方法 有参方法 无返回值
     * */
    public void test4(int m, int k) {

    }

    /**
     * 构造方法
     * */
    public StackTest() {

    }

    public void test5() {
        //第一类问题
        int i1 = 10;
        i1++;

        int i2 = 10;
        ++i2;

        //第二类问题
        int i3 = 10;
        int i4 = i3++;

        int i5 = 10;
        int i6 = ++i5;

        //第四类问题
        int i11 = 10;
        int i12 = 10;
        int i13 = i11++ + ++i12;
    }
}
