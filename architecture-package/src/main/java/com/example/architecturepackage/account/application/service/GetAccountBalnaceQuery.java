package com.example.architecturepackage.account.application.service;

import com.example.architecturepackage.account.application.port.in.GetAccountBalance;
import com.example.architecturepackage.account.application.port.out.LoadAccountPort;
import com.example.architecturepackage.account.domain.Account;
import com.example.architecturepackage.account.domain.Money;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class GetAccountBalnaceQuery implements GetAccountBalance {

    private final LoadAccountPort loadAccountPort;

    @Override
    public Money getAccountBalance(Account.AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
                .caculateBalance();
    }
}
