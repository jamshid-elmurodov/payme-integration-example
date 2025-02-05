package uz.paymeintegrationexample.domain.payme.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymeTransactionState {
    PAID(2),
    PENDING(1),
    PENDING_CANCELED(-1),
    PAID_CANCELED(-2),;

    private final int state;
}
