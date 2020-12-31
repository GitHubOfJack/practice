package com.jack;

/**
 * 构造方法有如下几种
 * Integer a = 1;  字节码底层是Integer.valueOf(1)实现的
 * Integer b = new Integer(1);
 * Integer.valueOf(1)  -127到128 会先查看缓存中是否有值,如果没有值new Integer(i)
 *
 * Integer.equals比较的是两个值是否相等
 * == 比较的是内存地址
 *
 * 如果一个Integer对象和int值做对比,则先调用Integer.intValue获取Integer的值，然后两个int值做对比
 * */
public class IntegerTest {
    public static void main(String[] args) {
        Integer a = 1;
        /*Integer e = 1;
        Integer b = new Integer(1);
        Integer d = new Integer(1);
        Integer c = Integer.valueOf(1);
        System.out.println(a == e);
        System.out.println(a == b);
        System.out.println(b == d);
        System.out.println(a == c);*/
        int f = 1;
        boolean bf = f == a;
    }
}
