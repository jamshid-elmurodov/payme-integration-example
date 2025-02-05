package uz.paymeintegrationexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;
import uz.paymeintegrationexample.repo.PaymeTransactionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final PaymeTransactionRepository paymeTransactionRepository;

    public PaymeTransaction save(PaymeTransaction build) {
        return paymeTransactionRepository.save(build);
    }

    public PaymeTransaction findById(String id) {
        return paymeTransactionRepository.findById(id).orElse(null);
    }

    public List<PaymeTransaction> findAllByCreateTimeIsBetween(Long from, Long to){
        return paymeTransactionRepository.findAllByCreateTimeIsBetween(from, to);
    }

    public boolean existsByOrderId(Long orderId) {
        return paymeTransactionRepository.existsByOrderId(orderId);
    }
}
