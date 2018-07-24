package be.dewolf.hofleverancier.ordertaking.integration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Override
    @Query("select o from Order o join fetch o.orderLines")
    List<Order> findAll();
}
