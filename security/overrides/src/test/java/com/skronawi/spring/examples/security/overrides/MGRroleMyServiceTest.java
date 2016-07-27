package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(roles = "MGR")
public class MGRroleMyServiceTest extends MyServiceTestBase {

    @Override
    protected boolean exceptionClassForUsr() {
        return true;
    }

    @Override
    protected boolean exceptionClassForAlsoMgr() {
        //access allowed, because the method-level annotation overrides the class-level
        return false ; //except for MyPreAuthorizedSecuredService !!!
    }
}
