package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@PreAuthorize("hasRole('USR')")
@Service
public class MyPreAuthorizedSecuredService implements MyService {

    @Override
    public String usr(){
        return "usr";
    }

    @Override
    @Secured("ROLE_MGR")
    public String alsoMgr(){
        return "usrAndMgr";
    }
}
