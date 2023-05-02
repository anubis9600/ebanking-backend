package templates.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.anubislab.ebankingbackend.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
