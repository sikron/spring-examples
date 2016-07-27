package com.skronawi.spring.examples.security.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@TestExecutionListeners(listeners = WithSecurityContextTestExecutionListener.class)
@ContextConfiguration(classes = TestConfig.class)
public abstract class MySecuredServiceTestBase extends AbstractTestNGSpringContextTests {

    @Autowired
    private MyService myService;

    protected abstract boolean exceptionClassForUsr();
    protected abstract boolean exceptionClassForAlsoMgr();

    @Test
    public void accessUsr(){
        try {
            myService.usr();
        } catch (Exception catchedE){
            if (!exceptionClassForUsr()){
                Assert.fail();
            }
        }
    }

    @Test
    public void accessAlsoMgr(){
        try {
            myService.alsoMgr();
        } catch (Exception catchedE){
            if (!exceptionClassForAlsoMgr()){
                Assert.fail();
            }
        }
    }
}
