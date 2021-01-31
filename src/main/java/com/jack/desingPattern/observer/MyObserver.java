package com.jack.desingPattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式
 *      1 当对象的某种状态发生了变化之后，一个或者多个观察者观察到相应的改变之后，作出相应的动作
 *
 *      spring中的多个listener
 */
public class MyObserver {
    public static void main(String[] args) {
        Subject subject = new Subject();
        subject.add(new Observer());
        subject.change();
    }
}

class Subject {
    private List<Observer> observerList;

    public Subject() {
        this.observerList = new ArrayList<>();
    }

    public void add(Observer observer) {
        observerList.add(observer);
    }

    public void change() {
        System.out.println("state changed");
        notifyObservers();
    }

    void notifyObservers() {
        for (Observer observer : observerList) {
            observer.doSomething();
        }
    }
}

class Observer {
    public void doSomething() {
        System.out.println("do something");
    }
}
