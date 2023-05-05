package tech.anubislab.ebankingbackend.dtos;

import tech.anubislab.ebankingbackend.enums.OpertarionType;

import java.util.Date;

import lombok.Data;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OpertarionType type;
    private String description;
}