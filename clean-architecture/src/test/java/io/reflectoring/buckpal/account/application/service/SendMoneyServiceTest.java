package io.reflectoring.buckpal.account.application.service;

import io.reflectoring.buckpal.account.application.port.in.SendMoneyCommand;
import io.reflectoring.buckpal.account.application.port.out.AccountLock;
import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.reflectoring.buckpal.account.domain.Account;
import io.reflectoring.buckpal.account.domain.Account.AccountId;
import io.reflectoring.buckpal.account.domain.Money;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class SendMoneyServiceTest {

	private final LoadAccountPort loadAccountPort =
			Mockito.mock(LoadAccountPort.class);

	private final AccountLock accountLock =
			Mockito.mock(AccountLock.class);

	private final UpdateAccountStatePort updateAccountStatePort =
			Mockito.mock(UpdateAccountStatePort.class);

	private final SendMoneyService sendMoneyService =
			new SendMoneyService(loadAccountPort, accountLock, updateAccountStatePort, moneyTransferProperties());

	@Test
	void givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased() {

		AccountId sourceAccountId = new AccountId(41L);
		Account sourceAccount = givenAnAccountWithId(sourceAccountId);

		AccountId targetAccountId = new AccountId(42L);
		Account targetAccount = givenAnAccountWithId(targetAccountId);

		givenWithdrawalWillFail(sourceAccount);
		givenDepositWillSucceed(targetAccount);

		SendMoneyCommand command = new SendMoneyCommand(
				sourceAccountId,
				targetAccountId,
				Money.of(300L));

		boolean success = sendMoneyService.sendMoney(command);

		assertThat(success).isFalse();

		then(accountLock).should().lockAccount(eq(sourceAccountId));
		then(accountLock).should().releaseAccount(eq(sourceAccountId));
		then(accountLock).should(times(0)).lockAccount(eq(targetAccountId));
	}

	@Test
	void transactionSucceeds() {

		// Given
		// 유스케이스의 입력으로 사용
		Account sourceAccount = givenSourceAccount();
		Account targetAccount = givenTargetAccount();

		givenWithdrawalWillSucceed(sourceAccount);
		givenDepositWillSucceed(targetAccount);

		Money money = Money.of(500L);

		SendMoneyCommand command = new SendMoneyCommand(
				sourceAccount.getId().get(),
				targetAccount.getId().get(),
				money);

		// When
		// 유스케스를 실행하기 위한 메서드 호출
		boolean success = sendMoneyService.sendMoney(command);

		assertThat(success).isTrue();

		AccountId sourceAccountId = sourceAccount.getId().get();
		AccountId targetAccountId = targetAccount.getId().get();

		// Then
		// 트랜잭션이 성공적이었는지 확인하기
		then(accountLock).should().lockAccount(eq(sourceAccountId));
		then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
		then(accountLock).should().releaseAccount(eq(sourceAccountId));

		then(accountLock).should().lockAccount(eq(targetAccountId));
		then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
		then(accountLock).should().releaseAccount(eq(targetAccountId));

		thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
	}

	private void thenAccountsHaveBeenUpdated(AccountId... accountIds){
		ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
		then(updateAccountStatePort).should(times(accountIds.length))
				.updateActivities(accountCaptor.capture());

		List<AccountId> updatedAccountIds = accountCaptor.getAllValues()
				.stream()
				.map(Account::getId)
				.map(Optional::get)
				.collect(Collectors.toList());

		for(AccountId accountId : accountIds){
			assertThat(updatedAccountIds).contains(accountId);
		}
	}

	private void givenDepositWillSucceed(Account account) {
		given(account.deposit(any(Money.class), any(AccountId.class)))
				.willReturn(true);
	}

	private void givenWithdrawalWillFail(Account account) {
		given(account.withdraw(any(Money.class), any(AccountId.class)))
				.willReturn(false);
	}

	private void givenWithdrawalWillSucceed(Account account) {
		given(account.withdraw(any(Money.class), any(AccountId.class)))
				.willReturn(true);
	}

	private Account givenTargetAccount(){
		return givenAnAccountWithId(new AccountId(42L));
	}

	private Account givenSourceAccount(){
		return givenAnAccountWithId(new AccountId(41L));
	}

	private Account givenAnAccountWithId(AccountId id) {
		Account account = Mockito.mock(Account.class);
		given(account.getId())
				.willReturn(Optional.of(id));
		given(loadAccountPort.loadAccount(eq(account.getId().get()), any(LocalDateTime.class)))
				.willReturn(account);
		return account;
	}

	private MoneyTransferProperties moneyTransferProperties(){
		return new MoneyTransferProperties(Money.of(Long.MAX_VALUE));
	}

}
