package com.example.architecturepackage.account.adapter.in.web;

import com.example.architecturepackage.account.application.port.in.SendMoneyCommand;
import com.example.architecturepackage.account.application.port.in.SendMoneyUseCase;
import com.example.architecturepackage.account.domain.Account;
import com.example.architecturepackage.account.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    public void sendMoney(
            @PathVariable("sourceAccountId") Long sourceAccountId,
            @PathVariable("targetAccountId") Long targetAccountId,
            @PathVariable("amount") Long amount
    ){
        SendMoneyCommand command = new SendMoneyCommand(
                new Account.AccountId(sourceAccountId),
                new Account.AccountId(targetAccountId),
                Money.of(amount)
        );
        sendMoneyUseCase.sendMoney(command);

    }
}
