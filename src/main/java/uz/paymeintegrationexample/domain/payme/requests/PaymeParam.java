package uz.paymeintegrationexample.domain.payme.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymeParam {
    private String id;
    private PaymeAccount account;
    private Long amount;
    private Long time;
    private Integer reason;
    private Long from;
    private Long to;
}
