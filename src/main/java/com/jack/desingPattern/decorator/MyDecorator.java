package com.jack.desingPattern.decorator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * 装饰器模式：
 *      主要点：
 *          1 装饰器类也需要实现原始类，然后在装饰器中有一个属性是原始类
 *          与适配器的不同是：适配器是替换原方法内容，而装饰器是增强功能
 *
 *          典型应用：java-io
 */
public class MyDecorator {
    public static void main(String[] args) throws FileNotFoundException {
        Cake cake = new EggCake();
        cake = new SausageCake(cake);


        FileInputStream fis = new FileInputStream("/Users/a0003/logs/ons.log");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);

    }
}

interface Cake {
    void addDecorator();
}

class EggCake implements Cake {

    @Override
    public void addDecorator() {
        System.out.println("啥也没有的鸡蛋饼");
    }
}

class SausageCake implements Cake {
    private Cake cake;

    public SausageCake(Cake cake) {
        this.cake = cake;
    }


    @Override
    public void addDecorator() {
        cake.addDecorator();
        System.out.println("老板！加个香肠！");
    }
}