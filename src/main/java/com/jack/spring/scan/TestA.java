package com.jack.spring.scan;

import com.jack.spring.Phone;
import com.jack.spring.event.MyApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author 马钊
 * @date 2020-07-22 10:06
 */
@Component
@Lazy
//@Scope("prototype")
public class TestA {
    //@Autowired
    private TestB testB;

    @Qualifier("phoneFactoryBean")
    @Autowired
    private Phone phone;

    public TestB getTestB() {
        return testB;
    }

    public void setTestB(TestB testB) {
        this.testB = testB;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    @EventListener
    public void handEvent(MyApplicationEvent myApplicationEvent) {
        System.out.println(myApplicationEvent.getSource());
    }
}
