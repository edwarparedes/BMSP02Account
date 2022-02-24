package com.nttdata.account.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Transaction {
    private String id;
    private String type;
    private LocalDateTime creationTime;
    private Double amount;
    private String accountId;
}
