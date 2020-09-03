package com.jack.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class Service2Impl implements Service2 {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void doInsert() {
        jdbcTemplate.update("insert into a(name, code) values('s2', 's2')");
        System.out.println(1/1);
    }
}
