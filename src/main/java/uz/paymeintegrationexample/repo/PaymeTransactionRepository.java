package uz.paymeintegrationexample.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.paymeintegrationexample.domain.entity.PaymeTransaction;

import java.util.List;

public interface PaymeTransactionRepository extends JpaRepository<PaymeTransaction, String> {
    boolean existsByOrderId(Long orderId);

    List<PaymeTransaction> findAllByCreateTimeIsBetween(Long from, Long to);
}