package com.skronawi.spring.examples.valid.mvc;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan
public class TestConfig {

//    @Bean
//    public LocalValidatorFactoryBean localValidatorFactoryBean() {
//        return new LocalValidatorFactoryBean();
//    }

//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        return new MethodValidationPostProcessor();
//    }


    // !!!!!! only the ResourceBundleMessageSource really works

//    @Bean
//    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource(){
//        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource =
//                new ReloadableResourceBundleMessageSource();
//        reloadableResourceBundleMessageSource.setBasename("classpath:messages");
//        return reloadableResourceBundleMessageSource;
//    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("msg");
        return messageSource;
    }


//    @Bean
//    public LocaleResolver localeResolver(){
//        return new FixedLocaleResolver(Locale.GERMAN);
//    }
}
