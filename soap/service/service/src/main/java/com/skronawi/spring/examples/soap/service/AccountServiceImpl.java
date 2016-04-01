package com.skronawi.spring.examples.soap.service;

import com.blog.samples.webservices.Account;
import com.blog.samples.webservices.EnumAccountStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class AccountService.
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final Map<String, Account> accounts = new HashMap<>();

    /**
     * Gets the account details.
     *
     * @param accountNumber the account number
     * @return the account details
     */
    public Account getAccountDetails(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @PostConstruct
    public void init() {

        //test data
        Account account = new Account();
        account.setAccountNumber("12345");
        account.setAccountStatus(EnumAccountStatus.ACTIVE);
        account.setAccountName("Joe Bloggs");
        account.setAccountBalance(3400);
        accounts.put(account.getAccountNumber(), account);
    }
}
