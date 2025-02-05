package uz.paymeintegrationexample.domain.payme.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckTransactionResult {
    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("perform_time")
    private Long performTime;

    @JsonProperty("cancel_time")
    private Long cancelTime;

    private String transaction;

    private Integer state;

    private Integer reason;

    public static CheckTransactionResult from(PaymeTransaction transaction) {
        return CheckTransactionResult.builder()
                .createTime(transaction.getCreateTime())
                .performTime(transaction.getPerformTime())
                .cancelTime(transaction.getCancelTime())
                .transaction(transaction.getId())
                .state(transaction.getState().getState())
                .reason(transaction.getReason())
                .build();
    }
}
