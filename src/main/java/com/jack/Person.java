package com.jack;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author: 马钊
 * @Date: 2019/11/22 21:46
 */
public class Person implements InitializingBean, DisposableBean {
    private Integer age;
    private String name;

    public Person() {
        System.out.println("构造器...");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean...");
    }

    public void init() {
        System.out.println("initMethod...");
    }

    public void destroyMethod() {
        System.out.println("destroyMethod...");
    }

    @PostConstruct
    public void init2() {
        System.out.println("@PostConstruct...");
    }

    @PreDestroy
    public void destroy2() {
        System.out.println("@PreDestroy...");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("            \"age\":").append(age);
        sb.append(",             \"name\":\"").append(name).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
