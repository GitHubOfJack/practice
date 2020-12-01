package com.jack.jvm;

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
 *
 * 加载：把文件从某个地方加载进JVM中（本地、JAR、网络、数据库等等）
 * 如果要对class内容进行加解密，在此处进行，重写findClass方法，数组类型不是通过classloader加载的，
 * 是JVM直接在内存中动态构建的，但是数组相关类型是通过classloader加载的
 *
 * 连接
 *      验证:
 *          如果校验失败会报错:VerifyError
 *          1 文件格式验证：魔数验证、版本验证
 *          2 元数据验证：是否有父类、是否继承了final类、是否重写了final方法
 *          3 字节码验证：验证方法中Code属性中的字节码是否合法
 *          4 符号引用验证（发生在解析阶段）：通过全限定名是否能找到相应的类，符号引用对应的类、方法、属性能否被访问等
 *      准备
 *      解析
 *
 * 初始化
 *
 * */
public class ClassLoaderTest extends ClassLoader{

}
