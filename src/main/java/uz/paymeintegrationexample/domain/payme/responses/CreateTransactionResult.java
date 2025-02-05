package uz.paymeintegrationexample.domain.payme.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTransactionResult {
    @JsonProperty("create_time")
    private Long createTime;

    private String transaction;

    private int state;

    public static CreateTransactionResult from(PaymeTransaction transaction) {
        return CreateTransactionResult.builder()
                .createTime(transaction.getCreateTime())
                .transaction(transaction.getId())
                .state(transaction.getState().getState())
                .build();
    }
}
