package uz.paymeintegrationexample.domain.payme.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.paymeintegrationexample.domain.payme.util.Message;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymeError {
    private Integer code;
    private Message message;
    private String data;

    public PaymeError(uz.paymeintegrationexample.domain.payme.enums.PaymeError error, String data) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.data = data;
    }
}
