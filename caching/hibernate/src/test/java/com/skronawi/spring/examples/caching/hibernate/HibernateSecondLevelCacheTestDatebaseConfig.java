package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan
@PropertySource("classpath:database.properties")
@EnableJpaRepositories
@EnableTransactionManagement //necessary for the @Transactional !
public class HibernateSecondLevelCacheTestDatebaseConfig {

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
        factory.setPackagesToScan("com.skronawi.spring.examples.caching.hibernate");
        factory.setDataSource(dataSource());

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", dbHbm2ddl);
        properties.setProperty("hibernate.show_sql", dbShowSql);
        properties.setProperty("hibernate.dialect", dbDialect);

        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_minimal_puts", "true");
        properties.setProperty("hibernate.cache.use_query_cache", "true");

        //hazelcast does not support hibernate 5
        //http://extendsobject.blogspot.com/2013/10/hibernate-caching-with-hazelcast.html
        //http://docs.hazelcast.org/docs/3.5/manual/html/hibernate.html
        //http://docs.hazelcast.org/docs/2.3/manual/html/ch13.html
//        properties.setProperty("hibernate.cache.hazelcast.use_lite_member", "true");
//        properties.setProperty("hibernate.cache.region.factory_class", "com.hazelcast.hibernate.HazelcastCacheRegionFactory");
////        properties.setProperty("hibernate.cache.region.factory_class", "com.hazelcast.hibernate.HazelcastLocalCacheRegionFactory");

        //use ehcache instead
        //https://examples.javacodegeeks.com/enterprise-java/hibernate/second-level-cache-hibernate-example/
        properties.setProperty("hibernate.current_session_context_class", "thread");
        properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        properties.setProperty("net.sf.ehcache.configurationResourceName", "ehcache.xml");

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

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
