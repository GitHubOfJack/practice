package com.jack.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author 马钊
 * @date 2020-07-29 18:25
 */
@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public void doInsert() {
        //mock insert
        jdbcTemplate.update("insert into a(name, code) values('1', '1')");
        //System.out.println(1/0);
    }
}
