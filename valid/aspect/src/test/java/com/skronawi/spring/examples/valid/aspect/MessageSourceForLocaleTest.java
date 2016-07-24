package com.skronawi.spring.examples.valid.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Locale;

@ContextConfiguration(classes = MessageSourceForLocaleTest.Config.class)
public class MessageSourceForLocaleTest extends AbstractTestNGSpringContextTests {

    public static class Config {
        @Bean
        public MessageSource messageSource() {
            ReloadableResourceBundleMessageSource messageSource =
                    new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("msg");
//            messageSource.setDefaultEncoding("UTF-8");
            return messageSource;
        }
    }

    @Autowired
    private MessageSource messageSource;

    @DataProvider
    public static Object[][] values() {
        return new Object[][]{
//                new Object[]{null, "default message"}, //then the system-locale is used, which is en_US
                new Object[]{null, "us english message"},
                new Object[]{Locale.ENGLISH, "us english message"},
                new Object[]{Locale.GERMAN, "deutsche nachricht"},
        };
    }

    @Test(dataProvider = "values")
    public void test(Locale locale, String message) throws Exception {
        String value = messageSource.getMessage("key", null, locale);
        Assert.assertEquals(value, message);
    }
}
