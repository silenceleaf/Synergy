package org.zjy.synergy.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by zjy on 14-1-13.
 */

@Configuration
public class PersistenceConfig {
    @Autowired
    private DataSource pooledDataSource;

    @Value("hibernate.dialect")
    //@Value("${hibernate.dialect}")
    private String hibernateDialect;
    //@Value("${hibernate.show_sql}")
    @Value("hibernate.show_sql")
    private String hibernateShowSql;
    //@Value("${hibernate.format_sql}")
    @Value("hibernate.format_sql")
    private String hibernateFormatSql;
    //@Value("${hibernate.use_sql_comments}")
    @Value("hibernate.use_sql_comments")
    private String hibernateUseSqlComments;


    @Bean
    public SessionFactory createSessionFactory () {
        LocalSessionFactoryBuilder sessionFactoryBuilder = new LocalSessionFactoryBuilder(pooledDataSource);
        // some hibernate configurations
//        sessionFactoryBuilder.setProperty("hibernate.dialect", hibernateDialect);
//        sessionFactoryBuilder.setProperty("hibernate.show_sql", hibernateShowSql);
//        sessionFactoryBuilder.setProperty("hibernate.format_sql", hibernateFormatSql);
//        sessionFactoryBuilder.setProperty("hibernate.use_sql_comments", hibernateUseSqlComments);

        // scan all entity classes
        sessionFactoryBuilder.scanPackages("org.zjy.synergy.entity");

        return sessionFactoryBuilder.buildSessionFactory();
    }



}
