package uz.paymeintegrationexample.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.paymeintegrationexample.domain.payme.enums.PaymeTransactionState;
import uz.paymeintegrationexample.domain.payme.requests.PaymeParam;

@Entity(name = "payme_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymeTransaction {
    @Id
    private String id;

    private Long orderId;

    private Long amount;

    private long createTime;

    private long performTime;

    private long cancelTime;

    @Enumerated(EnumType.STRING)
    private PaymeTransactionState state;

    private Integer reason;

    public static PaymeTransaction from(PaymeParam params) {
        return PaymeTransaction.builder()
                .id(params.getId())
                .orderId(params.getAccount().getOrderId())
                .amount(params.getAmount())
                .createTime(System.currentTimeMillis())
                .state(PaymeTransactionState.PENDING)
                .build();
    }
}
