package tech.anubislab.ebankingbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.anubislab.ebankingbackend.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
