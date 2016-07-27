package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(roles = {"MGR","USR"})
public class MGRandUSRroleMyServiceTest extends MyServiceTestBase {

    @Override
    protected boolean exceptionClassForUsr() {
        return false;
    }

    @Override
    protected boolean exceptionClassForAlsoMgr() {
        return false ;
    }
}
