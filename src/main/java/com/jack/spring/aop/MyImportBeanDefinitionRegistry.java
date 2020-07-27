package com.jack.spring.aop;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author 马钊
 * @date 2020-07-27 10:34
 */
public class MyImportBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(Buss.class);
        registry.registerBeanDefinition("buss", rootBeanDefinition);

        RootBeanDefinition myAspcetj = new RootBeanDefinition();
        myAspcetj.setBeanClass(MyAspectj.class);
        registry.registerBeanDefinition("myAspcetj", myAspcetj);
    }
}
