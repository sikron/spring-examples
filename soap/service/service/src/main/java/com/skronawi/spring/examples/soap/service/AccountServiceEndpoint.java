package com.skronawi.spring.examples.soap.service;

import com.blog.samples.webservices.Account;
import com.blog.samples.webservices.AccountDetailsRequest;
import com.blog.samples.webservices.AccountDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * The Class AccountService.
 */
@Endpoint
public class AccountServiceEndpoint {

    //    private static final String TARGET_NAMESPACE = "http://com/blog/samples/webservices/accountservice";
    private static final String TARGET_NAMESPACE = "http://webservices.samples.blog.com";

    @Autowired
    private AccountService accountService;

    /**
     * Gets the account details.
     *
     * @param request the account details request
     * @return the account details
     */
    @PayloadRoot(localPart = "AccountDetailsRequest", namespace = TARGET_NAMESPACE)
    public
    @ResponsePayload
    AccountDetailsResponse getAccountDetails(@RequestPayload AccountDetailsRequest request) {

        AccountDetailsResponse response = new AccountDetailsResponse();
        Account account = accountService.getAccountDetails(request.getAccountNumber());
        response.setAccountDetails(account);
        return response;
    }
}