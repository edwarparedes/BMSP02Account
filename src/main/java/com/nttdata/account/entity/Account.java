package com.nttdata.account.entity;

import com.nttdata.account.model.Holder;
import com.nttdata.account.model.Signatory;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Account {
    @Id
    private String id;
    private String accountNumber;
    private Double maintenanceFee;
    private int movementLimit;
    private Double balance;
    private LocalDateTime creationTime;

    List<Holder> holders;//titulares
    List<Signatory> signatories;// firmantes

    private String customerId;
    private String productId;
}
