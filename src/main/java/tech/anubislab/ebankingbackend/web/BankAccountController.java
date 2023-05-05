package tech.anubislab.ebankingbackend.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.anubislab.ebankingbackend.dtos.AccountHistoryDTO;
import tech.anubislab.ebankingbackend.dtos.AccountOperationDTO;
import tech.anubislab.ebankingbackend.dtos.BankAccountDTO;
import tech.anubislab.ebankingbackend.dtos.CurrentBankAccountDTO;
import tech.anubislab.ebankingbackend.dtos.SavingBankAccountDTO;
import tech.anubislab.ebankingbackend.exceptions.BalanceNotSufficientException;
import tech.anubislab.ebankingbackend.exceptions.BankAccountNotFoundException;
import tech.anubislab.ebankingbackend.exceptions.CustomerNotFoundException;
import tech.anubislab.ebankingbackend.services.BankAccountService;

@RestController
public class BankAccountController {
    
    BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService){
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException{
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> bankAccountList(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable("id") String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageoperations")
    public AccountHistoryDTO getAccountHistory(
                @PathVariable String accountId, 
                @RequestParam(name = "page", defaultValue = "0") int page,
                @RequestParam(name = "size", defaultValue = "2") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/accounts/savingaccount/save")
    public SavingBankAccountDTO addSavingBankAccount(@RequestParam(name = "balance") double initialBalance, 
                                @RequestParam(name = "interest") double interestRate, 
                                @RequestParam(name = "customer") Long customerId) throws CustomerNotFoundException{
        
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    @PostMapping("/accounts/currentaccount/save")
    public CurrentBankAccountDTO addCurrentBankAccount(@RequestParam(name = "balance") double initialBalance, 
                                @RequestParam(name = "overdraft") double overDraft, 
                                @RequestParam(name = "customer") Long customerId) throws CustomerNotFoundException{
        
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/accounts/operations/transfer")
    public String transfer(@RequestParam(name = "idcredit") String accountIdSource,
                            @RequestParam(name = "iddebit") String accountIdDestination, 
                            @RequestParam(name = "amount") double amount) throws BankAccountNotFoundException, BalanceNotSufficientException{
        
        return bankAccountService.transfer(accountIdSource, accountIdDestination, amount);
    }

}