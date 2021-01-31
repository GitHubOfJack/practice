package com.jack.desingPattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理模式
 *      静态代理(类似装饰器)
 *
 *      动态代理：
 *          jdk动态代理（底层是asm字节码技术）
 *          1 必须是接（可以通过指定参数查看生成的代理类）
 *          2 必须实现InvocationHandler类,重写invoke方法，不是必须继承抽象类
 *              inovke方法参数详解,1 生成的代理类本身 2 当前执行的方法 3 当前执行的方法的参数
 *          3 通过Proxy.newProxyInstance方法创建代理对象
 *              newProxyInstance方法的参数详解 1 加载目标类的classloader 2 加载目标类的接口 3 使用那种代理策略
 *
 */
public class JdkDynamicProxy {
    public static void main(String[] args) {
        Target target = new TargetImpl();
        ProxyImpl proxy = new ProxyImpl(target);
        System.out.println(proxy);
        //这个方法可以封装在ProxyImpl类中
        Target o = (Target) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), proxy);
        o.doTarget();
    }
}

interface Target {
    void doTarget();
}

class TargetImpl implements Target {

    @Override
    public void doTarget() {
        System.out.println("do real something");
    }
}

class ProxyImpl implements InvocationHandler {

    private Target target;

    public ProxyImpl(Target target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //System.out.println(proxy);
        Object invoke = method.invoke(target, args);
        System.out.println("do something");
        return invoke;
    }
}
