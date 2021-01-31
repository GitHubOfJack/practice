package com.jack.desingPattern.factory;

import javafx.scene.effect.Effect;

/**
 *  抽象工厂方法：
 *      主要点：
 *          与工厂方法最大的不同是，工厂方法每个工厂只能生产一个产品，而工厂方法可以生成一个产品族
 */
public class AbstractFactory {
    public static void main(String[] args) {
        EFactory eFactory = new HuaWeiAllFactory();
        eFactory.createMac();
        eFactory.createPhone();
    }

}

interface Mac {
    Mac createMac();
}

class HuaWeiMac implements Mac {

    @Override
    public Mac createMac() {
        return new HuaWeiMac();
    }
}

class AppleMac implements Mac {

    @Override
    public Mac createMac() {
        return null;
    }
}

interface EFactory {
    Phone createPhone();
    Mac createMac();
}

class HuaWeiAllFactory implements EFactory {

    @Override
    public Phone createPhone() {
        return new HuaWei();
    }

    @Override
    public Mac createMac() {
        return new HuaWeiMac();
    }
}

class AppleAllFactory implements EFactory {

    @Override
    public Phone createPhone() {
        return new Apple();
    }

    @Override
    public Mac createMac() {
        return new AppleMac();
    }
}
