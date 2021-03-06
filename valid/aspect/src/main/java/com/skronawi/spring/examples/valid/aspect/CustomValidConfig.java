package com.skronawi.spring.examples.valid.aspect;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class CustomValidConfig {

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource =
//                new ReloadableResourceBundleMessageSource();
//
////        messageSource.setBasename("classpath:validationMessages");
//        messageSource.setBasename("validationMessages");
//
////        messageSource.setFallbackToSystemLocale(true);
//        messageSource.setDefaultEncoding("UTF-8");
////        messageSource.setCacheSeconds(3600);
//
//        return messageSource;
//    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //different from "validationMessages" and "messages" on purpose to see, whether this aspect stuff really works
        messageSource.setBasename("my-messages");
        return messageSource;
    }
}
