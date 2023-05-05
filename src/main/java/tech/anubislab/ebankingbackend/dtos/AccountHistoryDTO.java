package tech.anubislab.ebankingbackend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class AccountHistoryDTO {

    private String accountId;
    private String nameCustomer;
    private double balance;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOs;
}