package com.jack.desingPattern.proxy;

import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib动态代（底层还是asm字节码技术）
 *          1 可以为具体类做代理，原理是继承，代理类必须实现MethodInterceptor类，重写intercept方法
 *              intercept方法详解:1 代理类本身 2 拦截方法 3 方法参数 4 增强的方法
 *          2 类不能为final的，否则直接报错，方法可以为final，但是final的方法不会代理，非final的方法可以被代理
 *          2 非private、非final、非static可以生成代理
 *
 *          cglib的原理是这样，它生成一个继承B的类型C（代理类），这个代理类持有一个MethodInterceptor，
 *          我们setCallback时传入的。 C重写所有B中的方法（方法名一致），然后在C中，
 *          构建名叫“CGLIB”+“$父类方法名$”的方法（下面叫cglib方法，所有非private的方法都会被构建），
 *          方法体里只有一句话super.方法名()，可以简单的认为保持了对父类方法的一个引用，方便调用。
 *
 * 这样的话，C中就有了重写方法、cglib方法、父类方法（不可见），还有一个统一的拦截方法（增强方法intercept）。
 * 其中重写方法和cglib方法肯定是有映射关系的。
 *
 * C的重写方法是外界调用的入口（LSP原则），它调用MethodInterceptor的intercept方法，
 * 调用时会传递四个参数，第一个参数传递的是this，代表代理类本身，第二个参数标示拦截的方法，
 * 第三个参数是入参，第四个参数是cglib方法，intercept方法完成增强后，
 * 我们调用cglib方法间接调用父类方法完成整个方法链的调用。
 *
 *
 * 代理对象调用this.setPerson方法->调用拦截器->methodProxy.invokeSuper->CGLIB$setPerson$0->被代理对象setPerson方法
 *
 */
public class CglibProxy {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/a0003/logs/cglib");
        MyProxy proxy = new MyProxy(new CglibTarget());
        Object proxy1 = proxy.createProxy();
        ((CglibTarget)proxy1).doSomething();
    }


}

class CglibTarget{
    public void doSomething() {
        System.out.println("what");
    }
}

class MyProxy implements MethodInterceptor {
    private CglibTarget cglibTarget;
    public MyProxy(CglibTarget cglibTarget) {
        this.cglibTarget = cglibTarget;
    }

    public Object createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CglibTarget.class);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("enhance");

        return methodProxy.invokeSuper(cglibTarget, objects);
    }
}
