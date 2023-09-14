package com.example.architecturepackage.account.adapter.in.web;


import com.example.architecturepackage.account.application.port.in.SendMoneyUseCase;
import com.example.architecturepackage.account.application.service.GetAccountBalnaceQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountCotroller {

    // Service
    private final GetAccountBalnaceQuery;
    private final listAccountsQuery;
    private final loadAccountsQuery;

    // 해당 인터페이스를 구현코드를 Ioc컨테이너에서 의존성 주입을 받아서 초기화시킴.
    private final SendMoneyUseCase sendMoneyUseCase;
    private final CreateAccountUseCase createAccountUseCase;

    @GetMapping("/accounts")
    public List<AccountResource> listAccounts(){

    }

    @GetMapping("/accounts/{accountId}")
    public AccountResource getAccount(@PathVariable("accountId") Long accountId){

    }

    @GetMapping("/accounts/{accountId}/balance")
    public long getAccountBalance(@PathVariable("accountId") Long accountId){

    }

    @PostMapping("/accounts")
    public AccountResource createAccount(AccountResource account) {

    }

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    public void sendMoney(
            @PathVariable("sourceAccountId") Long sourceAccountId,
            @PathVariable("targetAccountId") Long targetAccountId,
            @PathVariable("amount") Long amount
    ){

    }
}
