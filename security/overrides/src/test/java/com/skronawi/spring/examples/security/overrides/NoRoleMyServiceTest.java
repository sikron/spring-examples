package com.skronawi.spring.examples.security.overrides;

public class NoRoleMyServiceTest extends MyServiceTestBase {

    @Override
    protected boolean exceptionClassForUsr() {
        return true;
    }

    @Override
    protected boolean exceptionClassForAlsoMgr() {
        return true ;
    }
}
