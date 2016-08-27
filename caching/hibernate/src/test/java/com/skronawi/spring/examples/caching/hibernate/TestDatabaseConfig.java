package com.skronawi.spring.examples.caching.hibernate;

import com.skronawi.spring.examples.caching.hibernate.firstlevel.DefaultCacheConfig;
import com.skronawi.spring.examples.caching.hibernate.secondlevel.SecondLevelCacheConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Properties;

@EnableTransactionManagement //necessary for the @Transactional !
@Configuration
@PropertySource("classpath:database.properties")
@EnableJpaRepositories
//exclude the configs by default, add them to the context in the test-classes as needed. otherwise they would interfere with each other
@ComponentScan(excludeFilters =
    @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {DefaultCacheConfig.class, SecondLevelCacheConfig.class}))
public class TestDatabaseConfig {

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
    public EntityManagerFactory entityManagerFactory(Properties jpaProperties) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.skronawi.spring.examples.caching.hibernate");
        factory.setDataSource(dataSource());

        /*
        https://docs.oracle.com/javaee/7/api/javax/persistence/SharedCacheMode.html
        http://docs.oracle.com/javaee/6/tutorial/doc/gkjjj.html

        SharedCacheMode.ALL means: All entities and entity-related state and data are cached.
        items can also be selectively in- and excluded from caching!!
         */
        factory.setSharedCacheMode(SharedCacheMode.ALL);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", dbHbm2ddl);
        properties.setProperty("hibernate.show_sql", dbShowSql);
        properties.setProperty("hibernate.dialect", dbDialect);

        jpaProperties.entrySet()
                .forEach(entry -> properties.setProperty((String) entry.getKey(), (String) entry.getValue()));

        factory.setJpaProperties(properties);

        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
