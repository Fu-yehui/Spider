package com.roger.QuestionBankSpider.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.roger.QuestionBankSpider.dao.DaoMarker;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackageClasses = {DaoMarker.class})
@PropertySource("classpath:jdbc.properties")
public class DaoConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource druidDataSource() throws SQLException {
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("username"));
        dataSource.setPassword(env.getProperty("password"));
        dataSource.setFilters(env.getProperty("filters"));
        dataSource.setMaxActive(20);
        dataSource.setInitialSize(1);
        dataSource.setMaxWait(6000);
        dataSource.setMinIdle(1);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxOpenPreparedStatements(20);
        dataSource.setAsyncInit(true);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(new ClassPathResource("classpath:mybatis-config.xml"));
        factoryBean.setMapperLocations(new ClassPathResource[]{new ClassPathResource("classpath:mapper/*.xml")});
        factoryBean.setTypeAliasesPackage("com.roger.SeckillOL.entity");
        SqlSessionFactory sessionFactory = factoryBean.getObject();
        return sessionFactory;
    }
}
