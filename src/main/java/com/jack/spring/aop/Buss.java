package com.jack.spring.aop;

/**
 * @author 马钊
 * @date 2020-07-27 09:26
 */
public class Buss {
    @MyLogAspect
    public void doBuss() {
        System.out.println(1/1);
    }
}
