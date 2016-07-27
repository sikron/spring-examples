package com.skronawi.spring.examples.security.simple;

public class NoRoleMySecuredServiceTest extends MySecuredServiceTestBase {

    @Override
    protected boolean exceptionClassForUsr() {
        return true;
    }

    @Override
    protected boolean exceptionClassForAlsoMgr() {
        return true ;
    }
}
