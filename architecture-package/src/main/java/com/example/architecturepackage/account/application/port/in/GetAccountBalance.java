package com.example.architecturepackage.account.application.port.in;

import com.example.architecturepackage.account.domain.Account;
import com.example.architecturepackage.account.domain.Money;

public interface GetAccountBalance {

    public Money getAccountBalance(Account.AccountId accountId);
}
