package uz.paymeintegrationexample.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.paymeintegrationexample.domain.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}