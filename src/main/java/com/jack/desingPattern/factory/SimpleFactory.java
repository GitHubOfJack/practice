package com.jack.desingPattern.factory;

/**
 * 1 简单工厂
 *
 *      主要点：
 *          1 有一个抽象产品接口
 *          2 有多个抽象产品实现类
 *          3 有一个抽象工厂-该工厂可以通过if-else或者switch语句生产对应的产品，或者就是多个方法，每个方法返回一种产品
 *
 *      参考spring中bean对象的获取
 *      AbstractBeanFactory.getBean(String name)方法
 *
 */
public class SimpleFactory {
    Phone createPhone(String type) {
        Phone phone;
        switch (type) {
            case "Hw" :
                phone = new HuaWei();
                return phone;
            case "Apple" :
                phone = new Apple();
                return phone;
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        SimpleFactory simpleFactory = new SimpleFactory();
        simpleFactory.createPhone("HuaWei");
    }
}

interface Phone {
    void create();
}

class HuaWei implements Phone {

    @Override
    public void create() {
        System.out.println("HW");
    }
}

class Apple implements Phone {

    @Override
    public void create() {
        System.out.println("Apple");
    }
}




