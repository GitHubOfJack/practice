package com.jack.spring;

import com.jack.spring.aop.MyImportBeanDefinitionRegistry;
import com.jack.spring.factorybean.PhoneFactoryBean;
import com.jack.spring.transaction.DBConfig;
import com.jack.spring.transaction.DataSourceBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 马钊
 * @date 2020-07-20 09:33
 */
@Configuration
@ComponentScan({"com.jack.spring.scan"})
@EnableAspectJAutoProxy
@Import({DBConfig.class, PhoneFactoryBean.class, MyImportBeanDefinitionRegistry.class})
//@Import({PhoneFactoryBean.class, MyImportBeanDefinitionRegistry.class})
@EnableTransactionManagement
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
