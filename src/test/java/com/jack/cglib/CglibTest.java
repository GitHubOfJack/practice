package com.jack.cglib;


import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author 马钊
 * @date 2020-07-30 10:19
 */
public class CglibTest {
    @Test
    public void test() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target/cglib");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(NormalClass.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                Object o1 = methodProxy.invokeSuper(o, objects);
                System.out.println("after");
                return o1;
            }
        });
        NormalClass normalClass = (NormalClass) enhancer.create();
        normalClass.publicMethod();
        normalClass.staticMethod();
    }
}
