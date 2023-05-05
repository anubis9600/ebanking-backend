package tech.anubislab.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import tech.anubislab.ebankingbackend.dtos.AccountHistoryDTO;
import tech.anubislab.ebankingbackend.dtos.AccountOperationDTO;
import tech.anubislab.ebankingbackend.dtos.BankAccountDTO;
import tech.anubislab.ebankingbackend.dtos.CurrentBankAccountDTO;
import tech.anubislab.ebankingbackend.dtos.CustomerDTO;
import tech.anubislab.ebankingbackend.dtos.SavingBankAccountDTO;
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
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Enregistrement d'un nouveau client");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        
        log.info("Client enregistre");
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public Customer getCustomeById(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Ce client n'existe pas");

        return customer;
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = getCustomeById(customerId);
        
        if(customer == null) throw new CustomerNotFoundException("Ce client n'existe pas");

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = getCustomeById(customerId);

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(savedBankAccount);
    }

    @Override
    public CustomerDTO getCustomer(Long idCustomer) throws CustomerNotFoundException{
        Customer customer = customerRepository.findById(idCustomer)
                        .orElseThrow(()->new CustomerNotFoundException("Ce client n'existe pas"));

        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer ->dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());

        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(()->new BankAccountNotFoundException("Ce compte bancaire n'existe pas"));
                
        if(bankAccount instanceof CurrentAccount){
            CurrentAccount currentAccount = (CurrentAccount)bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }else {
            SavingAccount savingAccount = (SavingAccount)bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        }

    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOs = bankAccounts.stream().map(bankAccount ->{
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount)bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            }else{
                CurrentAccount currentAccount = (CurrentAccount)bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOs;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                                    orElseThrow(()->new BankAccountNotFoundException("Ce compte bancaire n'existe pas"));

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
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                                    orElseThrow(()->new BankAccountNotFoundException("Ce compte bancaire n'existe pas"));

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
    public String transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer vers "+accountIdDestination);
        credit(accountIdDestination, amount, "Transfer de "+accountIdSource);

        return "Le tranfert effectue";
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Mis a jour du client");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Client modifiee avec succes");
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public Long deleteCustomer(CustomerDTO customerDTO, @PathVariable("id") Long customerId){
        customerRepository.deleteById(customerId);
        log.info("Le client avec identifiant "+customerId+"a ete supprime");

        return customerId;
    }

    /**
     * @param accountId
     * @return
     */
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findBybankAccountId(accountId);
        
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount == null) throw new BankAccountNotFoundException("Ce compte n'existe pas ");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId,  PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> collect = accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOs(collect);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setNameCustomer(bankAccount.getCustomer().getName());
        accountHistoryDTO.setTotalPage(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }

}