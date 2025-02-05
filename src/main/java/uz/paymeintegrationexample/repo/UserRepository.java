package uz.paymeintegrationexample.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.paymeintegrationexample.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}