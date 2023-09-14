package com.example.architecturepackage.account.application.port.out;

import com.example.architecturepackage.account.domain.Account;

import java.time.LocalDateTime;

public interface LoadAccountPort {
    Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate);
}
