package tech.anubislab.ebankingbackend.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import tech.anubislab.ebankingbackend.dtos.CustomerDTO;
import tech.anubislab.ebankingbackend.services.BankAccountService;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){

        return bankAccountService.listCustomers();
    }
    
}
