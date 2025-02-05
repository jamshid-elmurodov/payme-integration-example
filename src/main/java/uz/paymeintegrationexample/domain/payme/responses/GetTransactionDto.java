package uz.paymeintegrationexample.domain.payme.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;
import uz.paymeintegrationexample.domain.payme.requests.PaymeAccount;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTransactionDto {
    private String id;

    private Long time;

    private Long amount;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("perform_time")
    private Long performTime;

    @JsonProperty("cancel_time")
    private Long cancelTime;

    private PaymeAccount account;

    private String transaction;

    private Integer state;

    private Integer reason;

    public static GetTransactionDto from(PaymeTransaction paymeTransaction) {
        return GetTransactionDto.builder()
                .id(paymeTransaction.getId())
                .time(paymeTransaction.getCreateTime())
                .amount(paymeTransaction.getAmount())
                .createTime(paymeTransaction.getCreateTime())
                .performTime(paymeTransaction.getPerformTime())
                .cancelTime(paymeTransaction.getCancelTime())
                .account(new PaymeAccount(paymeTransaction.getOrderId()))
                .transaction(paymeTransaction.getId())
                .state(paymeTransaction.getState().getState())
                .reason(paymeTransaction.getReason())
                .build();
    }
}
