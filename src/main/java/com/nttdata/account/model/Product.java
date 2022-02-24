package com.nttdata.account.model;

import lombok.Data;

import java.util.Date;
@Data
public class Product {
    private String name;
    private String type;
    private Double maintenanceFee;
    private int movementLimit;
    private Date day;
}
