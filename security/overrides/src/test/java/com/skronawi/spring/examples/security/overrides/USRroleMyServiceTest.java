package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(roles = "USR")
public class USRroleMyServiceTest extends MyServiceTestBase {

    @Override
    protected boolean exceptionClassForUsr() {
        return false;
    }

    @Override
    protected boolean exceptionClassForAlsoMgr() {
        //access NOT allowed, as for alsoMgr() the MGR role is needed
        return true ; //except for MyPreAuthorizedSecuredService !!!
    }
}
