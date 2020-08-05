package com.jack.spring.transaction;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;

/**
 * @author 马钊
 * @date 2020-07-29 18:26
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
@ComponentScan("com.jack.spring.transaction")
@EnableConfigurationProperties
public class DBConfig {
    @Autowired
    private DataSourceBean dataSourceBean;

    @Autowired
    private TransactionInterceptor transactionInterceptor;

    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceBean.getUrl());
        druidDataSource.setUsername(dataSourceBean.getName());
        druidDataSource.setPassword(dataSourceBean.getPwd());
        druidDataSource.setDriverClassName(dataSourceBean.getDclass());
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }


    //@Bean
    public TransactionManagementConfigurer transactionManagementConfigurer() {
        //transactionInterceptor.setTransactionManagerBeanName("platformTransactionManager");
        return new TransactionManagementConfigurer() {
            @Override
            public TransactionManager annotationDrivenTransactionManager() {
                return new DataSourceTransactionManager(dataSource());
            }
        };
    }
}
