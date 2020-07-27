package com.jack.spring.factorybean;

import com.jack.spring.Phone;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author 马钊
 * @date 2020-07-23 14:38
 */
public class PhoneFactoryBean implements FactoryBean<Phone> {
    @Override
    public Phone getObject() throws Exception {
        return new Phone();
    }

    @Override
    public Class<?> getObjectType() {
        return Phone.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
