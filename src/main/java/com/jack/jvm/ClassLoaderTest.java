package com.jack.jvm;

import com.jack.Person;

/**
 * 如果extends  ClassLoader
 * ClassLoader.loadClass();双亲委派在这个方法中实现，底层会调用findClass方法
 * ClassLoader.findClass();如果想要重写ClassLoader，只需要重写该方法即可
 *
 *
 * AppClassLoader extends URLClassLoader是Launcher静态内部类
 *
 * ExtClassLoader extends URLClassLoader是Launcher静态内部类
 *
 * class对象中有指针指向方法区的类元数据，也有指针指向对应的classLoader对象
 *
 * classLoader对象中有该Loader加载过的所有Class信息
 *
 *
 *
 * 加载：(会生成堆中的Class对象)把文件从某个地方加载进JVM中（本地、JAR、网络、数据库等等）
 * 如果要对class内容进行加解密，在此处进行，重写findClass方法，数组类型不是通过classloader加载的，
 * 是JVM直接在内存中动态构建的，但是数组相关类型是通过classloader加载的
 *
 *
 * BootstrapClassLoader(加载jre/lib/rt.jar、resources.jar)
 * ExtClassLoader(加载jre/lib/ext目录下的jar包)
 * AppClassLoader
 *
 * 涉及到双亲委派：
 * 打破双亲委派：线程上下文类加载器
 *
 * SPI（Service Provider Interface）
 * DriverManager静态代码块中进行
 * ServiceLoader.load(Driver.class)会使用Thread属性中有一个名字是contextClassLoader的ClassLoader用来加载相关的类
 * 调用循环方法时：去META-INF/services/下面找Driver的文件
 * 文件的名称是：定义的接口类的名字如java.sq.Driver，文件内容是实现了该接口的类:包+类名称，多个实现类之间换行区分
 *
 *
 * 连接
 *      验证:
 *          如果校验失败会报错:VerifyError
 *          1 文件格式验证：魔数验证、版本验证
 *          2 元数据验证：是否有父类、是否继承了final类、是否重写了final方法
 *          3 字节码验证：验证方法中Code属性中的字节码是否合法
 *          4 符号引用验证（发生在解析阶段）：通过全限定名是否能找到相应的类，符号引用对应的类、方法、属性能否被访问等
 *      准备:
 *          1 一般情况下为静态变量赋0值（静态变量在1.8之后在堆中）
 *          2 如果是final常量&是基本数据类型或者字符串字面量，该field有constantvlaue属性，则在此时赋值
 *      解析:符号引用转化为直接引用
 *          符号引用：CONSTANT_Methodref_info\CONSTANT_Fieldref_info\
 *              CONSTANT_Class_info\CONSTANT_Integer_info\CONSTANT_Utf8_info\CONSTANT_NameAndType_Info
 *          直接引用:内存地址
 *
 * 初始化:执行clinit方法
 *      clinit方法是由编译器生成的，由所有类变量的赋值动作和静态代码块中的语句合并产生。(父类的clinit方法在子类的clinit方法之前调用)
 *      clinit方法不是必须的，没有赋值动作和静态代码块，则没有clinit方法。类执行clinit方法时，不会先执行接口的clinit方法（如果有的话）
 *      只有当接口变量被使用时才会调用.jvm必须保证一个类的clinit方法在多线程环境中被正确的加锁同步。而且clinit只会被jvm加载一次。
 *
 *
 *
 *
 * */
public class ClassLoaderTest extends ClassLoader{

    static {
        i = 1;
    }

    private static int i = 0;

    //有constantvlaue属性
    private static final int j = 1;

    private static String a = "a";

    //有constantvlaue属性
    private static final String b = "b";

    //没有constantvlaue属性
    private static final Person PERSON = new Person();



    public static void main(String[] args) {
        System.out.println(i);
    }

}
