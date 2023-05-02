package tech.anubislab.ebankingbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.anubislab.ebankingbackend.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
