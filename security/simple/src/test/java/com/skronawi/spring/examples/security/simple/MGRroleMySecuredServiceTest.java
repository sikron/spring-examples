package com.skronawi.spring.examples.security.simple;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(roles = "MGR")
public class MGRroleMySecuredServiceTest extends MySecuredServiceTestBase {

    @Override
    protected boolean exceptionClassForUsr() {
        return true;
    }

    @Override
    protected boolean exceptionClassForAlsoMgr() {
        return true ;
    }
}
