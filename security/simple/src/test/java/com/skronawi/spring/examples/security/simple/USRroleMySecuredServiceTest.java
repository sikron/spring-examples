package com.skronawi.spring.examples.security.simple;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(roles = "USR")
public class USRroleMySecuredServiceTest extends MySecuredServiceTestBase {

    @Override
    protected boolean exceptionClassForUsr() {
        return false;
    }

    @Override
    protected boolean exceptionClassForAlsoMgr() {
        return true ;
    }
}
