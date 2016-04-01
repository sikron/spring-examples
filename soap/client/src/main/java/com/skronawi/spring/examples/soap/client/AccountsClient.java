package com.skronawi.spring.examples.soap.client;

import com.blog.samples.webservices.AccountDetailsRequest;
import com.blog.samples.webservices.AccountDetailsResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class AccountsClient extends WebServiceGatewaySupport {

    public AccountDetailsResponse getAccountsResponse(String accountNumber) {
        AccountDetailsRequest request = new AccountDetailsRequest();
        request.setAccountNumber(accountNumber);
        AccountDetailsResponse response = (AccountDetailsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        request, new SoapActionCallback("http://localhost:8080/endpoints/AccountDetailsResponse"));
        return response;
    }
}
