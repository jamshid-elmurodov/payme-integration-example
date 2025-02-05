package uz.paymeintegrationexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.paymeintegrationexample.domain.entity.Order;
import uz.paymeintegrationexample.repo.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order findById(Long id){
        return orderRepository.findById(id).orElse(null);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }
}
