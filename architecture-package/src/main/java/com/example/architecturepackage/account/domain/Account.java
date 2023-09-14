package com.example.architecturepackage.account.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Account 엔티티는 실제 계좌의 현재 스냅숏을 제공한다.
 */
@Getter
public class Account {
    private AccountId id;
    private Money baselineBalance;
    private ActivityWindow activityWindow;

    public Money caculateBalance(){
        return Money.add(
                this.baselineBalance,
                this.activityWindow.calculateBalance(this.id));
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {
        if(!mayWithdraw(money)){
            return false;
        }

        Activity withdrawal = Activity.builder()
                .ownerAccountId(this.id)
                .sourceAccountId(this.id)
                .targetAccountId(targetAccountId)
                .timestamp(LocalDateTime.now())
                .money(money)
                .build();
        this.activityWindow.addActivity(withdrawal);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        return Money.add(
                this.caculateBalance(),
                money.negate()
        ).isPositive();
    }

    public boolean deposit(Money money, AccountId sourceAccountId){
        Activity deposit = Activity.builder()
                .ownerAccountId(this.id)
                .sourceAccountId(sourceAccountId)
                .targetAccountId(this.id)
                .timestamp(LocalDateTime.now())
                .money(money)
                .build();
        this.activityWindow.addActivity(deposit);
        return true;
    }


    @Getter
    @RequiredArgsConstructor
    public static class AccountId {
        private final Long value;
    }
}
