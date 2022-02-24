package com.nttdata.account.model;

import lombok.Data;

@Data
public class Customer {
    private String name;
    private String type;
    private String documentType;
    private String documentNumber;
}
