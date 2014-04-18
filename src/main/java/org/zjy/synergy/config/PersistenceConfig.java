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


    @Bean
    public SessionFactory createSessionFactory () {
        LocalSessionFactoryBuilder sessionFactoryBuilder = new LocalSessionFactoryBuilder(pooledDataSource);

        // scan all entity classes
        sessionFactoryBuilder.scanPackages("org.zjy.synergy.entity");

        return sessionFactoryBuilder.buildSessionFactory();
    }



}
