package com.jack.spring.aop;
// implements BizInterface
public class BizImpl extends BizSuper{
    @MyLogAspect
    public void doBiz() {
        System.out.println("doBizzzzz");
    }
    /*@MyLogAspect
    @Override
    public void doTest() {
        System.out.println("doTest");
    }*/

    @Override
    public void doSuper2() {
        System.out.println("doSupersssss2");
    }
}
