package uz.paymeintegrationexample.domain.payme.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymeRequest {
    private String method;
    private PaymeParam params;
}
