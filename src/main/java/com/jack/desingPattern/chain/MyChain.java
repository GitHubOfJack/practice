package com.jack.desingPattern.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任链模式
 *      主要点：
 *          实现方式一：每个实现类内部都持有下一个实现类的引用
 *          实现方式二：aop实现方式(每个方法都传入一个队列，或者持有这个队列的对象)
 */
public class MyChain {
    //采用方式二实现
    public static void main(String[] args) {
        Chain1 chain1 = new Chain1();
        Chain2 chain2 = new Chain2();
        ChainHolder ch = new ChainHolder();
        ch.add(chain1);
        ch.add(chain2);

        ch.doChain();
    }
}

class ChainHolder {
    List<Chain> chainList;

    int i = 0;

    public ChainHolder() {
        this.chainList = new ArrayList<>();
    }

    void add(Chain chain) {
        chainList.add(chain);
    }

    void doChain() {
        Chain chain = chainList.get(i);
        if (null == chain) {
            return;
        } else {
            chain.doChain(this);
        }
    }
}

interface Chain {
    void doChain(ChainHolder ch);
}

class Chain1 implements Chain {

    @Override
    public void doChain(ChainHolder ch) {
        System.out.println("do chain1");
        ch.doChain();

    }
}

class Chain2 implements Chain {

    @Override
    public void doChain(ChainHolder ch) {
        System.out.println("do chain2");
        ch.doChain();
    }
}
