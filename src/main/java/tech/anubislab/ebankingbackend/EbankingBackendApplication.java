package tech.anubislab.ebankingbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.anubislab.ebankingbackend.entities.*;
import tech.anubislab.ebankingbackend.enums.AccountStatus;
import tech.anubislab.ebankingbackend.enums.OpertarionType;
import tech.anubislab.ebankingbackend.exceptions.BalanceNotSufficientException;
import tech.anubislab.ebankingbackend.exceptions.BankAccountNotFoundException;
import tech.anubislab.ebankingbackend.exceptions.CustomerNotFoundException;
import tech.anubislab.ebankingbackend.repositories.AccountOperationRepository;
import tech.anubislab.ebankingbackend.repositories.BankAccountRepository;
import tech.anubislab.ebankingbackend.repositories.CustomerRepository;
import tech.anubislab.ebankingbackend.services.BankAccountService;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
/*
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return args -> {
            Stream.of("Bonheur", "Divine", "Julie", "Prince").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail((name+"@gmail.com").toLowerCase());
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());
					List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
					for (BankAccount bankAccount:bankAccounts) {
						bankAccountService.credit(bankAccount.getId(), 10000+Math.random()*120000, "Crédit");
					}
					for (BankAccount bankAccount:bankAccounts) {
						bankAccountService.debit(bankAccount.getId(), 1000+Math.random()*9000, "Débit");
					}
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
					throw new RuntimeException(e);
				}
			});

		};
	}

	// @Bean
	/*CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){
		return args -> {
			Stream.of("Bonheur", "Divine", "Julie").forEach(name->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail((name+"@gmail.com").toLowerCase());
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(cust -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.ACTIVATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(4.5);
				bankAccountRepository.save(savingAccount);

				});

			bankAccountRepository.findAll().forEach(acc->{
				for (int i = 0; i < 10; i++){
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setType(Math.random()>0.5?OpertarionType.DEBIT:OpertarionType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}*/

}