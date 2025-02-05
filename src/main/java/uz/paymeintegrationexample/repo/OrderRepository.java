package uz.paymeintegrationexample.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.paymeintegrationexample.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}