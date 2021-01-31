package com.jack.desingPattern.adapter;

/**
 * 适配器模式：
 *      1 把b方法的功能通过a方法包装之后给目标对象调用
 *
 *      spring中的适配器模式:aop中的增强器
 */
public class MyAdapter {
    public static void main(String[] args) {
        Target target = new Adapter();
        target.doTarget();
    }
}

interface Target{
    void doTarget();
}

class TargetImpl implements Target {

    @Override
    public void doTarget() {
        System.out.println("do target");
    }
}

class Adaptee {
    public void doAdaptee() {
        System.out.println("do adaptee");
    }
}

//可以通过继承获得，也可以通过当成属性对象获得
class Adapter extends Adaptee implements Target {

    @Override
    public void doTarget() {
        super.doAdaptee();
    }
}
