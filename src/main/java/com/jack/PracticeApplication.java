package com.jack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 给容器中注册组件；
 *    1,包扫描+组件标注注解（@Controller/@Service/@Repository/@Component）[自己写的类]
 *    	@ComponentScan注解用来标识扫描哪些类，但是这些类有相关注解才会加入容器，没有注解的并不会加入容器
 *    2,@Bean[导入的第三方包里面的组件]
 *    3,@Import[快速给容器中导入一个组件]
 *              1）、@Import(要导入到容器中的组件)；容器中就会自动注册这个组件，id默认是全类名
 *              2）、ImportSelector:返回需要导入的组件的全类名数组；
 *              3）、ImportBeanDefinitionRegistrar:手动注册bean到容器中
 *
 *
 * Tomcat启动流程
 * 在IOC的onRefresh方法中启动的
 * 核心思想是把DispatcherServlet放入到Tomcat容器中
 * Tomcat容器启动的时候会启动ServletContainerInitializer.onStartup方法
 * 有一个TomcatStarter类实现了ServletContainerInitializer，并且TomcatStarter中有一个ServletContextInitializer数组
 * TomcatStarter.onStartup方法会遍历数组中的ServletContextInitializer调用onStartup方法
 * 有一个DispatcherServletRegistrationBean的对象实现了ServletContextInitializer,
 * 在它的onstartup方法中把dispatcherServlet放入到了tomcat容器中
 *
 *
 *
 * */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@RestController
@Slf4j
//@PropertySource("classpath:jdbc.properties")
public class PracticeApplication implements BeanFactoryAware {
	private BeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
	}

	@RequestMapping("/test")
	public String test() {
		log.debug("debug测试");
		log.info("查看输出位置");
		return "success";
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
