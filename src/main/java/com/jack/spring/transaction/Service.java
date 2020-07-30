package com.jack.spring.transaction;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author 马钊
 * @date 2020-07-29 18:25
 */
@Transactional
public interface Service {
    void doInsert();
}
