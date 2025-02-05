package uz.paymeintegrationexample.domain.payme.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymeErrorResponse {
    private PaymeError error;
}
