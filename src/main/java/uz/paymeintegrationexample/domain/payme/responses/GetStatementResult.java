package uz.paymeintegrationexample.domain.payme.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStatementResult {
    private List<GetTransactionDto> transactions;
}
