package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Secured("ROLE_USR")
@Service
public class MySecuredPreAuthorizedService implements MyService {

    @Override
    public String usr(){
        return "usr";
    }

    @Override
    @PreAuthorize("hasRole('MGR')")
    public String alsoMgr(){
        return "usrAndMgr";
    }
}
