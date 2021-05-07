package com.roger.QuestionBankSpider.configuration;

import com.roger.QuestionBankSpider.service.ServiceMarker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackageClasses = {ServiceMarker.class})
@EnableTransactionManagement
@Import(DaoConfig.class)
public class ServiceConfig {

    @Bean
    public DataSourceTransactionManager TransactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
