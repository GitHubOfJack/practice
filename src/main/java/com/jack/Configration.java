package com.jack;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 马钊
 * @Date: 2019/11/22 21:45
 */
@Configuration
@ComponentScan("com.jack")
public class Configration {
    @Bean(value = "getPerson", initMethod="init", destroyMethod="destroyMethod")
    public Person person() {
        Person person = new Person();
        return person;
    }

    public static void main(String[] args) {
        //执行顺序  构造方法->@PostConstruct->InitializingBean->initMethod     @PreDestroy->DisposableBean->destroyMethod
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Configration.class);
        Object person = annotationConfigApplicationContext.getBean("getPerson");
        System.out.println(person);
        annotationConfigApplicationContext.close();
    }
}
