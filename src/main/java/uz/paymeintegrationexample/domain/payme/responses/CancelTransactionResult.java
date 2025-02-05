package uz.paymeintegrationexample.domain.payme.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelTransactionResult {
    private String transaction;

    @JsonProperty("cancel_time")
    private Long cancelTime;

    private int state;

    public static CancelTransactionResult from(PaymeTransaction paymeTransaction) {
        return CancelTransactionResult.builder()
                .transaction(paymeTransaction.getId())
                .cancelTime(paymeTransaction.getCancelTime())
                .state(paymeTransaction.getState().getState())
                .build();
    }
}
