package tech.anubislab.ebankingbackend.services;

import tech.anubislab.ebankingbackend.dtos.AccountHistoryDTO;
import tech.anubislab.ebankingbackend.dtos.AccountOperationDTO;
import tech.anubislab.ebankingbackend.dtos.BankAccountDTO;
import tech.anubislab.ebankingbackend.dtos.CurrentBankAccountDTO;
import tech.anubislab.ebankingbackend.dtos.CustomerDTO;
import tech.anubislab.ebankingbackend.dtos.SavingBankAccountDTO;
import tech.anubislab.ebankingbackend.entities.Customer;
import tech.anubislab.ebankingbackend.exceptions.BalanceNotSufficientException;
import tech.anubislab.ebankingbackend.exceptions.BankAccountNotFoundException;
import tech.anubislab.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO getCustomer(Long idCustomer) throws CustomerNotFoundException;
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    Long deleteCustomer(CustomerDTO customerDTO, Long customerId);
    Customer getCustomeById(Long customerId) throws CustomerNotFoundException;
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccountDTO> bankAccountList();
    List<AccountOperationDTO> accountHistory(String accountId);
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    String transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

}