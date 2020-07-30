package com.jack.cglib;

/**
 * @author 马钊
 * @date 2020-07-30 09:41
 */
public class NormalClass {
    private void privateMethod(){
        System.out.println("i am private");
    }

    public void publicMethod() {
        System.out.println("i am public");
    }

    public static void staticMethod() {
        System.out.println("i am static");
    }
}
