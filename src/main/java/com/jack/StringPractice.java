package com.jack;

/**
 * String 是final的  为什么？
 * 如果不是final的，那么hash值就会改变
 * 如果不是final的，那么安全性就有问题
 * final关键字   如果是static变量 要么在声明时指定，要么在静态代码块中指定，如果是成员变量 要么在声明时指定，要么在构造方法中指定 final修饰类 final修饰方法 final修饰变量
 * 底层结构是什么?
 * private final char value[]
 * +操作的原理
 * new StringBuilder()
 * 然后调用append()方法
 * 最后调用toString()方法
 * StringBuilder StringBuffer的区别
 * 字符串常量池、类的常量池、运行时常量池的关系
 * 字符串常量池在运行时常量池中（在1.7之前在运行时常量池中，在1.7之后在堆中）
 * intern()方法
 * 方法区是接口->永久区和元空间是实现
 *
 *
 * 1 String是否是不可变的？
 *   不可变是指什么：不可变是指对象的成员变量不可变，如果是基础类型，基础类型的值不可变，如果是引用类型，引用类型不能指向其他对象
 *   首先String是final的,不能被继承
 *   底层是private final char[]数组，final表明该指针指向的位置不会改变，private是用来控制权限，不给外部的对象改变此对象的内容的权限，
 *   同时也没有提供相应的修改char[]数组的对外的方法
 *   比如修改char[0]位置上的值
 *
 *   但是s="aa"之后s="bb"这种是，s指针发生了变化，但是"aa"对象并没有改变。很多String底层的方法，都是重新new一个String对象，然后把s指针指向新对象
 *
 * 2 是否可以改变String的不可变
 *   可以通过反射实现setAccessible(true)
 *   反射有几种类型？
 *   获取Class对象的三种方法
 *      1 对象.getClass()方法(getClass方法是Object中定义的方法)
 *      2 类.class()方法
 *      3 Class.forName()方法，里面是类的全路径名称
 *   通过反射生成实例对象的两种方法
 *      1 使用上一步获取到的class对象，调用newInstance()方法来创建实例
 *      2 通过上一步获取到的class对象，调用getConstructor()方法获得Constructor对象，再调用Constructor对象的newInstance()方法来获取实例
 *
 *        class.newInstance()方法只能反射无参构造方法，而且方法必须是可见的
 *        Constructor.newInstance()方法可以访问任务构造方法，也可以访问私有构造方法
 *
 *        A:获取所有构造方法
 *
 *          public Constructor[] getConstructors()
 *
 *          public Constructor[] getDeclaredConstructors()
 *
 *       B:获取单个构造方法
 *
 *          public Constructor getConstructor(Class... parameterTypes)
 *
 *          public Constructor getDeclaredConstructor(Class... parameterTypes)
 *
 *
 * 3 为什么要设计成不可变的？
 *   final是线程安全的，不需要考虑字符串的线程安全的问题
 *   HashMap中key=String时，如果可以改变，那就无法保证存入数据和取出数据的准确性
 *   字符串常量池只有不可变才有意义
 *
 *
 * 4 反射的优点和缺点
 *   为什么要使用反射：
 *      只有在运行时才真的知道需要new的是什么对象（比如Spring中的XML文件中定义的Bean对象，
 *      比如@ComponentScan中的对象，比如aop对象，只有在真正被用的时候才会通过反射获取，否则就只是BeanDefinition）
 *   优点是：提供了灵活性、扩展性，创建任何一个对象，不需要提前硬编码
 *   缺点是：
 *      性能问题：反射调用比较慢
 *          慢的原因：
 *              正常情况下调用方法，new对象，调用方法，就结束了。而采用反射调用方法，需要经历获取class对象，调用构造方法，获取对应的方法等流程，而且反射的代码是没有办法被jit优化的。
 *      安全问题：可以打破访问权限，访问或者修改私有方法或者属性
 *
 *
 *  java代码需要编译才能执行
 *      有三种编译方式：1前期编译器，把.java文件编译成.class文件
 *                   2运行时的即使编译器，把字节码文件编译成机器码的过程（Just In Time Compiler ）（反射的代码没有办法使用jit及时编译器）
 *                   3静态提前编译器（AOT 编译器，Ahead Of Time Compiler）把.java文件直接编译成机器（jkd9出现）
 *      解释执行：代码一行行解释执行，执行完毕，解释结果就丢弃了。（可以立马执行，但是效率低）
 *      编译执行：一次性翻译好，后面直接调用（不能立马执行，必须经过编译，但是效率高）（jit把热点代码进行编译执行）
 *
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
        /*StringPractice sp = new StringPractice(10);
        sp.m = 1000;*/
        String s1 = "aaa";
        String s2 = new String("aaa");
        String s3 = s2.intern();
        String s4 = new String("aaa");
        System.out.println(s1 == s2);//false
        System.out.println(s1 == s3);//true
        System.out.println(s2 == s4);//false
        System.out.println(StringPractice.i);
        String a = "b";
        String b = a + "b";
    }

    public void addString() {
        String s = new String("a") + new String("b");
    }

}
