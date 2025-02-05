package uz.paymeintegrationexample.domain.payme.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uz.paymeintegrationexample.domain.payme.enums.PaymeError;

@Getter
@AllArgsConstructor
public class PaymeException extends RuntimeException {
    private PaymeError error;
    private String data;

    public PaymeException(PaymeError error) {
        this.error = error;
    }
}
