package com.jack.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * 1 下载安装 java -jar arthas-boot.jar
 * 2 卸载 删除.arthas和logs
 * 3 常用命令：
 *      基本命令
 *          help 帮助命令
 *          cat 查看命令
 *          grep 条件过滤命令
 *          pwd 查看当前目录
 *          cls 清屏
 *
 *          session 查看当前链接
 *          reset 重置一个或者所有增强类
 *          version 版本信息
 *          quit 退出
 *          stop 停止服务
 *          keymap 快捷键
 *      jvm命令
 *          dashboard 仪表盘
 *          thread 查看一个或者多个线程信息
 *          jvm 查看内存信息
 *          sysprop 查看系统属性
 *
 *          sysenv 查看系统变量
 *          vmoption 查看虚拟机变量
 *          getstatic 查看类的静态变量
 *          ognl    查看类的变量 实例变量
 *
 *      class相关信息
 *          sc 查询类相关信息
 *          sm 查询方法相关信息
 *
 *          jad 反编译类信息
 *          mc 内存中把.java文件变成.class文件
 *          redefine 把.class文件加载入内存中（不能新增方法、属性）
 *
 *      dump 将已加载类的字节码文件保存到指定目录中
 *      classloader 获取类加载器信息
 *
 *      monitor 监控指定类中方法的执行情况（多长时间内，执行多少次，成功次数，失败次数，平均时间）
 *      watch 观察指定方法的调用情况（前执行，后执行，异常执行，返回执行，入参、出参、返回值、目标对象、异常）
 *      trace 对方法内部调用路径进行追踪，并输出方法路径上每个节点上的耗时
 *      stack 对出当前方法被调用的路径
 *      tt    记录指定方法每次调用的入参和返回信息
 *
 *      options 全局开关
 *
 *      profiler 生成火焰图
 *
 *
 *      如何查看静态变量
 *      1 查找加载该类的classloader
 *      sc -d com.example.demo.arthas.user.UserController | grep classLoaderHash
 *      2 输出上面的classloader加载的类的类变量
 *      ognl -c 1be6f5c3 @com.example.demo.arthas.user.UserController@logger
 *      如何调用静态方法
 *      ognl '@java.lang.System@out.println("hello ognl")'
 *      如何获取实例变量
 *      1 使用tt命令保存调用现场
 *      tt -t org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter invokeHandlerMethod
 *      2 调用实例变量的实例方法
 *      tt -i 1000 -w 'target.getApplicationContext().getBean("helloWorldService").getHelloMessage()'
 *
 *
 *      查看最繁忙的3个线程
 *      thread 查看所有线程
 *      thread tid 查看某个线程的堆栈信息
 *      thread -n 3 找出最繁忙的3个线程
 *      thread -n 3 -i 5000 查找5秒内最繁忙的线程
 *      thread -b 找出当前阻塞其他线程的线程
 *
 *      全局命令说明
 *      -x 展示结果属性遍历深度，默认是1
 *      -n 执行的次数
 *      -c classloader的hash值
 *      退出q,关闭stop
 *
 *
 *      loader
 *      clazz
 *      method
 *      target
 *      params
 *      returnObj
 *      throwExp
 *      isBefore
 *      isThrow
 *      isReturn
 *
 *      ognl表达式
 *      1 获取静态变量和方法
 *          @package.classname@methodname(param)
 *
 *      2 获取数组和集合中的值
 *          array[0] list[0]
 *
 *      3 获取map中的值
 *          map.keys 所有key
 *          map.values 所有value
 *          map['k'] key=k的值
 *
 *      4 过滤和投影(过滤与投影之间的差别：类比于数据库中的表，过滤是取行的操作，而投影是取列的操作)
 *          获取List中成绩及格的对象: "stus.{?#this.grade>=60}"
 *          获取List中成绩及格的对象的username stus.{?#this.grade>=60}.{username}
 *          获取List中成绩及格的第一个对象的username stus.{^#this.grade>=60}.{username}
 *          获取List中成绩及格的最后一个对象的username stus.{$#this.grade>=60}.{username}
 *
 *
 * */
public class ArthasTest {
    public static void main(String[] args) {
        List<Object> objectList = new ArrayList<>();

    }
}
