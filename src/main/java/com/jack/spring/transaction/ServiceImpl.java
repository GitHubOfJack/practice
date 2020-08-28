package com.jack.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 马钊
 * @date 2020-07-29 18:25
 */
@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Service2 service2;

    @Override
    public void doInsert() {
        //mock insert
        jdbcTemplate.update("insert into a(name, code) values('s1', 's1')");
        //try {
            service2.doInsert();
        //} catch (Exception e) {
            //System.out.println(e);
        //}

        System.out.println(1/1);
    }
}
