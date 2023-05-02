package tech.anubislab.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.anubislab.ebankingbackend.dtos.CustomerDTO;
import tech.anubislab.ebankingbackend.entities.*;
import tech.anubislab.ebankingbackend.enums.OpertarionType;
import tech.anubislab.ebankingbackend.exceptions.BalanceNotSufficientException;
import tech.anubislab.ebankingbackend.exceptions.BankAccountNotFoundException;
import tech.anubislab.ebankingbackend.exceptions.CustomerNotFoundException;
import tech.anubislab.ebankingbackend.mappers.BankAccountMapperImpl;
import tech.anubislab.ebankingbackend.repositories.AccountOperationRepository;
import tech.anubislab.ebankingbackend.repositories.BankAccountRepository;
import tech.anubislab.ebankingbackend.repositories.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private AccountOperationRepository accountOperationRepository;
    @Autowired
    private BankAccountMapperImpl dtoMapper;

//    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Enregistrement d'un nouveau client");
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomeById(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Ce client n'existe pas");

        return customer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = getCustomeById(customerId);

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
//        Customer customer = customerRepository.findById(customerId).orElse(null);
//
//        if (customer == null) throw new CustomerNotFoundException("Ce client n'existe pas");
        Customer customer = getCustomeById(customerId);

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer ->dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        /*
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        */
        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(()->new BankAccountNotFoundException("Ce compte bancaire n'existe pas"));

        return bankAccount;
    }

    @Override
    public List<BankAccount> bankAccountList() {
        return bankAccountRepository.findAll();
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount.getBalance() < amount) throw new BalanceNotSufficientException("Solde insuffisant");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OpertarionType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

        log.info("Le compte a été débité avec succès");
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OpertarionType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

        log.info("Le compte a été crédité avec succès");
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer vers "+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer de "+accountIdSource);
    }
}
