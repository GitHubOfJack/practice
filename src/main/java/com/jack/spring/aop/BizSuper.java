package com.jack.spring.aop;

public abstract class BizSuper {
    @MyLogAspect
    public void doSuper1() {
        System.out.println("doSuper1");
    }

    public abstract void doSuper2();
}
