package com.jack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * java spi机制
 * spi=service provider interface
 * api=application program interface
 *
 * 站在开发者的角度看，api是开发人员调用之后可以完成某个功能，而spi则是，需要又开发者完成，供框架调用。
 * API的实现是框架，SPI的实现是开发者
 *
 * SPI中一个核心的类    ServiceLoader
 * 文件放在META-INF下
 * 文件名字是：SPI接口名称
 * 文件内容是：实现了SPI接口的实现类（如果有多个，换行表示）
 *
 * 一个典型的SPI的应用场景是JVM加载JDBC的过程
 *      之前的开发方式中，需要程序员手动引入MYSQL类   Class.forName("com.mysql.jdbc.Driver")  这种情况AppClassLoader会加载该类
 *
 *      现在的方式中，不用再直接引入该类，而在JVM加载DriverManager(rt.jar包中的类)时，在DriverManager的静态代码块中使用ServiceLoader把
 *      META-INF/services下的java.sql.Driver文件的com.mysql.jdbc.Driver加载到JVM中
 *      此处有一个问题，DriverManager是BootstrapClassLoader加载的，com.mysql.jdbc.Driver需要AppClassLoader加载，是通过
 *      Thread.currentThread().getContextClassLoader()获取线程上下文加载器完成的--这个就是打破了双亲委派机制
 *
 *
 * 总结：SPI是通过ServiceLoader + META-INF + （固定文件格式）完成的
 *      打破双亲委派是通过ContextClassLoader实现的
 *
 *
 *  SPI的问题：
 *      不能按需加载，需要遍历所有的实现，并实例化
 *
 *  延伸一个问题：DUBBO的SPI机制:
 *                  通过ExtensionLoader + META-INF/dubbo/internal + (固定文件格式)  文件名称是:DUBBO的接口名称   文件内容支持多行记录  每行记录key=DUBBO的接口实现类
 *               SPRING的SPI机制:
 *                  通过SpringFactoriesLoader + META-INF + spring.factories(文件名字是固定的，其它两个都是接口名字) 文件内容是  key=value,value  key是接口的全名  value是接口的实现
 * */
public class SPIPractice {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //1.加载驱动(开发推荐的方式)
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","test","test");
    }
}
