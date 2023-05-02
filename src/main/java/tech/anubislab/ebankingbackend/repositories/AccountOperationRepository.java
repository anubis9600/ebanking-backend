package tech.anubislab.ebankingbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.anubislab.ebankingbackend.entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
