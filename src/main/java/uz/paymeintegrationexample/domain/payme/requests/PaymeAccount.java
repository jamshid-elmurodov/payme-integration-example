package uz.paymeintegrationexample.domain.payme.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class PaymeAccount {
    @JsonProperty("order_id")
    private Long orderId;
}
