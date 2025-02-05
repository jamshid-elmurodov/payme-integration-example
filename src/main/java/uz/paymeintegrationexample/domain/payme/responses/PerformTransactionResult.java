package uz.paymeintegrationexample.domain.payme.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PerformTransactionResult {
    private String transaction;

    @JsonProperty("perform_time")
    private Long performTime;

    private int state;

    public static PerformTransactionResult from(PaymeTransaction transaction) {
        return PerformTransactionResult.builder()
                .transaction(transaction.getId())
                .performTime(transaction.getPerformTime())
                .state(transaction.getState().getState())
                .build();
    }
}
