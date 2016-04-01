package com.skronawi.spring.examples.soap.client;

import com.blog.samples.webservices.AccountDetailsResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RunSoapClient {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ClientAppConfig.class);
        ctx.refresh();

        AccountsClient accountsClient = ctx.getBean(AccountsClient.class);

        AccountDetailsResponse response = accountsClient.getAccountsResponse("12345");

        System.out.println(response.getAccountDetails().getAccountNumber());
        System.out.println(response.getAccountDetails().getAccountBalance());
        System.out.println(response.getAccountDetails().getAccountName());
        System.out.println(response.getAccountDetails().getAccountStatus());
    }
}
