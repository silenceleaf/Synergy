package org.zjy.synergy.config;

import com.mchange.v2.c3p0.DataSources;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.zjy.service.base.PropertiesService;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by zjy on 14-1-13.
 */

@Configuration
public class PersistenceConfig {
    @Autowired
    private PropertiesService propertiesService;

    @Bean
    public SessionFactory createSessionFactory () {
        // make sure driver class exists
        String driverClass = propertiesService.getProperty("driverClass");
        if(driverClass != null) {
            try {
                Class.forName(driverClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        DataSource pooledDataSource = null;
        try {
            DataSource unpooledDataSource = DataSources.unpooledDataSource(
                    propertiesService.getProperty("jdbcUrl"),
                    propertiesService.getProperty("userName"),
                    propertiesService.getProperty("password"));
           pooledDataSource = DataSources.pooledDataSource(unpooledDataSource, propertiesService);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalSessionFactoryBuilder sessionFactoryBuilder = new LocalSessionFactoryBuilder(pooledDataSource);
        // some hibernate configurations
        sessionFactoryBuilder.setProperty("hibernate.dialect", propertiesService.getProperty("hibernate.dialect"));
        sessionFactoryBuilder.setProperty("hibernate.show_sql", propertiesService.getProperty("hibernate.show_sql"));
        sessionFactoryBuilder.setProperty("hibernate.format_sql", propertiesService.getProperty("hibernate.format_sql"));
        sessionFactoryBuilder.setProperty("hibernate.use_sql_comments", propertiesService.getProperty("hibernate.use_sql_comments"));

        // scan all entity classes
        sessionFactoryBuilder.scanPackages("org.zjy.entity");

        return sessionFactoryBuilder.buildSessionFactory();
    }



}
