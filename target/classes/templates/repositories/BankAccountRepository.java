package templates.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.anubislab.ebankingbackend.entities.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
