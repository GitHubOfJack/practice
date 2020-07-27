package com.jack.spring.scan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author 马钊
 * @date 2020-07-22 10:06
 */
@Component
@Lazy
//@Scope("prototype")
public class TestB {
    //@Autowired
    private TestA testA;


    public TestA getTestA() {
        return testA;
    }

    public void setTestA(TestA testA) {
        this.testA = testA;
    }

    @Bean
    public TestC testC() {
        return new TestC();
    }
}
