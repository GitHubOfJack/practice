package com.jack.spring;

import com.jack.spring.aop.MyImportBeanDefinitionRegistry;
import com.jack.spring.factorybean.PhoneFactoryBean;
import org.springframework.context.annotation.*;

/**
 * @author 马钊
 * @date 2020-07-20 09:33
 */
@Configuration
@ComponentScan({"com.jack.spring.scan"})
@EnableAspectJAutoProxy
@Import({PhoneFactoryBean.class, MyImportBeanDefinitionRegistry.class})
public class MyConfig {
    @Bean
    public Car createCar() {
        Car car = new Car();
        car.setName("BMW");
        car.setSpeed(300);
        return car;
    }

    @Bean
    public PhoneFactoryBean phoneFactoryBean() {
        return new PhoneFactoryBean();
    }

    //@Bean
    //@Lazy
    //@Scope("singleton")
    /*public TestA testA() {
        return new TestA();
    }*/

    //@Bean
    //@Lazy
    //@Scope("singleton")
    /*public TestB testB() {
        //return new TestB();
    }*/
}
