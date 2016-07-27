package com.skronawi.spring.examples.security.overrides;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@TestExecutionListeners(listeners = WithSecurityContextTestExecutionListener.class)
@ContextConfiguration(classes = TestConfig.class)
public abstract class MyServiceTestBase extends AbstractTestNGSpringContextTests {

    @Autowired
    private MyService myService;

    protected abstract boolean exceptionClassForUsr();
    protected abstract boolean exceptionClassForAlsoMgr();

    @Test
    public void accessUsr(){

        if (!exceptionClassForUsr()){
            myService.usr();
        } else {
            try {
                myService.usr();
                Assert.fail();
            } catch (Exception e){
                //ok
            }
        }
    }

    @Test
    public void accessAlsoMgr(){

        if (!exceptionClassForAlsoMgr()){
            myService.alsoMgr();
        } else {
            try {
                myService.alsoMgr();
                Assert.fail();
            } catch (Exception e){
                //ok
            }
        }
    }
}
