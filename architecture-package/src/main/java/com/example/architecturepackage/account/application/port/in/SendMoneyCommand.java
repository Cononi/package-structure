package com.example.architecturepackage.account.application.port.in;

import com.example.architecturepackage.common.SelfValidating;
import com.example.architecturepackage.account.domain.Account.AccountId;
import com.example.architecturepackage.account.domain.Money;
import lombok.Getter;

import javax.validation.constraints.NotNull;

// 입력 모델(Input Model)
@Getter
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {
    @NotNull
    private final AccountId sourceAccountId;;
    @NotNull
    private final AccountId tagetAccountId;
    @NotNull
    private final Money money;

    public SendMoneyCommand(AccountId sourceAccountId, AccountId tagetAccountId, Money money) {
        this.sourceAccountId = sourceAccountId;
        this.tagetAccountId = tagetAccountId;
        this.money = money;
        this.validateSelf();
    }
}
