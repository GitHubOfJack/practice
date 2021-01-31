package com.jack.desingPattern.factory;

/**
 *  工厂方法
 *      要点:
 *          1 有一个抽象产品接口
 *          2 有多个抽象产品的实现类
 *          3 有一个抽象工厂接口，抽象工厂接口中有一个生产抽象产品的方法
 *          4 抽象工厂有多个实现类
 */
public class FactoryMethod {

    public static void main(String[] args) {
        Factory factory = new AppleFactory();

        factory.createPhone();
    }

}

interface Factory {
    Phone createPhone();
}

class HuaWeiFactory implements Factory{

    @Override
    public Phone createPhone() {
        return new HuaWei();
    }
}

class AppleFactory implements Factory {

    @Override
    public Phone createPhone() {
        return new Apple();
    }
}
