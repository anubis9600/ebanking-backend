package tech.anubislab.ebankingbackend.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tech.anubislab.ebankingbackend.dtos.CustomerDTO;
import tech.anubislab.ebankingbackend.entities.Customer;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
//        customerDTO.setId(customerDTO.getId());
//        customerDTO.setName(customer.getName());
//        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
