package com.example.architecturepackage.account.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 계좌의 대한 모든 입금과 출근은 Activity 엔티티에 포착된다.
 */
@Getter
@Builder
@RequiredArgsConstructor
public class Activity {
    private ActivityId id;

    /**
     * The account that owns this activity.
     */
    @NonNull
    private final Account.AccountId ownerAccountId;

    /**
     * The debited account.
     */
    @NonNull
    private final Account.AccountId sourceAccountId;

    /**
     * The credited account.
     */
    @NonNull
    private final Account.AccountId targetAccountId;

    /**
     * The timestamp of the activity.
     */
    @NonNull
    private final LocalDateTime timestamp;

    /**
     * The money that was transferred between the accounts.
     */
    @NonNull
    private final Money money;

    @RequiredArgsConstructor
    public static class ActivityId {
        private final Long value;
    }
}
