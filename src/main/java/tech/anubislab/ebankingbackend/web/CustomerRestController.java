package tech.anubislab.ebankingbackend.web;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import tech.anubislab.ebankingbackend.dtos.CustomerDTO;
import tech.anubislab.ebankingbackend.exceptions.CustomerNotFoundException;
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

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable("id") Long idCustomer) throws CustomerNotFoundException{
        return bankAccountService.getCustomer(idCustomer);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{idCustomer}")
    public CustomerDTO updateCustomer(@PathVariable Long idCustomer,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(idCustomer);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public Long deleteCustomer(CustomerDTO customerDTO, @PathVariable("id") Long CustomerId){
        return bankAccountService.deleteCustomer(customerDTO, CustomerId);
    }

}
