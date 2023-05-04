package tech.anubislab.ebankingbackend.dtos;

import java.util.Date;

import lombok.Data;
import tech.anubislab.ebankingbackend.enums.AccountStatus;

@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interstRate;

}
