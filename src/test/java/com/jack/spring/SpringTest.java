package com.jack.spring;

import com.jack.spring.aop.Buss;
import com.jack.spring.event.MyApplicationEvent;
import com.jack.spring.factorybean.PhoneFactoryBean;
import com.jack.spring.scan.TestA;
import com.jack.spring.scan.TestB;
import com.jack.spring.transaction.DataSourceBean;
import com.jack.spring.transaction.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.transaction.BeforeTransaction;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author 马钊
 * @date 2020-07-20 09:38
 */
public class SpringTest {
    @Test
    public void doTest() {
        //XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new ClassPathResource(""));
        //ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("");
        System.out.println(System.getenv());
        System.out.println(System.getProperties());
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyConfig.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Stream.of(beanDefinitionNames).forEach(x-> System.out.println("----------"+x));
        applicationContext.publishEvent(new MyApplicationEvent("aaa"));
        TestA bean = applicationContext.getBean(TestA.class);
        System.out.println(bean);
        System.out.println(bean.getPhone());
        TestB beanb = applicationContext.getBean(TestB.class);
        System.out.println(beanb);
        Object bean1 = applicationContext.getBean("&phoneFactoryBean");
        System.out.println(bean1);
        Object bean2 = applicationContext.getBean("phoneFactoryBean");
        System.out.println(bean2);
        Map<String, PhoneFactoryBean> beansOfType = applicationContext.getBeansOfType(PhoneFactoryBean.class);
        beansOfType.entrySet().forEach(x-> System.out.println(x));
        System.out.println(applicationContext.getBean("com.jack.spring.factorybean.PhoneFactoryBean").equals(applicationContext.getBean("phoneFactoryBean")));
        System.out.println(applicationContext.getBean("&com.jack.spring.factorybean.PhoneFactoryBean").equals(applicationContext.getBean("&phoneFactoryBean")));


        Buss buss = (Buss) applicationContext.getBean("buss");
        buss.doBuss();

        Object bean3 = applicationContext.getBean("dataSourceBean");
        System.out.println(bean3);
        System.out.println(bean3.getClass());
        DataSourceBean bean4 = applicationContext.getBean(DataSourceBean.class);
        System.out.println(bean3 == bean4);

        Service bean5 = applicationContext.getBean(Service.class);
        bean5.doInsert();

        DataSource bean6 = applicationContext.getBean(DataSource.class);
        System.out.println(bean6);
        try {
            System.out.println(bean6.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
