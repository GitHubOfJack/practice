package com.jack.spring.transaction;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 马钊
 * @date 2020-07-29 18:38
 */
@ConfigurationProperties(prefix = "jdbc")
@Data
@Component
public class DataSourceBean {
    private String dclass;
    private String name;
    private String pwd;
    private String url;
}
