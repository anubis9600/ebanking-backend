package templates.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.anubislab.ebankingbackend.entities.AccountOperation;
import tech.anubislab.ebankingbackend.entities.BankAccount;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
