package com.skronawi.spring.examples.jpa.impl;

import com.skronawi.spring.examples.jpa.impl.tests.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration  //a java configuration, which Spring can use to bootstrap the application
@ComponentScan({Constants.BASE_PACKAGE}) //Spring scans the package for annotated classes
@PropertySource("classpath:database.properties") //Spring reads these properties and can inject them into @Value annotated fields
@EnableJpaRepositories //Spring provides implementations of @Repository annotated interfaces
public class TestConfig {

    @Value("${db.driverClassName}")
    private String dbDriverClassName;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String dbUsername;
    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.ddl-auto:update}")
    private String dbHbm2ddl;
    @Value("${db.show-sql:false}")
    private String dbShowSql;
    @Value("${hibernate.dialect}")
    private String dbDialect;

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(dbDriverClassName);
        driverManagerDataSource.setPassword(dbPassword);
        driverManagerDataSource.setUrl(dbUrl);
        driverManagerDataSource.setUsername(dbUsername);
        return driverManagerDataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(Constants.BASE_PACKAGE);
        factory.setDataSource(dataSource());

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", dbHbm2ddl);
        properties.setProperty("hibernate.show_sql", dbShowSql);
        properties.setProperty("hibernate.dialect", dbDialect);

        factory.setJpaProperties(properties);

        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    /*
    this is needed for the @Value annotation to have effect
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
