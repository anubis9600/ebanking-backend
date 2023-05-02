package tech.anubislab.ebankingbackend.services;

import tech.anubislab.ebankingbackend.dtos.CustomerDTO;
import tech.anubislab.ebankingbackend.entities.BankAccount;
import tech.anubislab.ebankingbackend.entities.CurrentAccount;
import tech.anubislab.ebankingbackend.entities.Customer;
import tech.anubislab.ebankingbackend.entities.SavingAccount;
import tech.anubislab.ebankingbackend.exceptions.BalanceNotSufficientException;
import tech.anubislab.ebankingbackend.exceptions.BankAccountNotFoundException;
import tech.anubislab.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    Customer saveCustomer(Customer customerDto);
    Customer getCustomeById(Long customerId) throws CustomerNotFoundException;
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccount> bankAccountList();
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

}
