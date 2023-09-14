package com.example.architecturepackage.account.application.port.in;

public interface SendMoneyUseCase {

    public boolean sendMoney(SendMoneyCommand command);
}
