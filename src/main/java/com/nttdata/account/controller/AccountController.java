package com.nttdata.account.controller;

import com.mongodb.MongoWriteException;
import com.nttdata.account.entity.Account;
import com.nttdata.account.model.Customer;
import com.nttdata.account.model.Product;
import com.nttdata.account.model.Transaction;
import com.nttdata.account.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    IAccountService service;

    @GetMapping
    public Flux<Account> getAccounts(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Account>> getAccountById(@PathVariable("id") String id){
        return service.getAccountById(id)
                .map(savedMessage -> ResponseEntity.ok(savedMessage))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    Mono<Account> postAccount(@RequestBody Account account){
        return service.save(account);
    }

    @PutMapping
    Mono<Account> updAccount(@RequestBody Account account){
        return service.update(account);
    }

    @DeleteMapping("/{id}")
    void dltAccount(@PathVariable("id") String id){
        service.delete(id);
    }

    @GetMapping("/customer/{customerId}")
    public Mono<Customer> getCustomer(@PathVariable("customerId") String customerId){
        return service.getCustomer(customerId);
    }

    @GetMapping("/product/{productId}")
    public Mono<Product> getProduct(@PathVariable("productId") String productId){
        return service.getProduct(productId);
    }

    @GetMapping("/checkbalance/{accountNumber}")
    public Mono<Account> checkBalance(@PathVariable("accountNumber") String accountNumber){
        return service.checkBalance(accountNumber);
    }

    @GetMapping("/transactions/{accountId}")
    public Flux<Transaction> getTransactions(@PathVariable("accountId") String accountId){
        return service.getTransactions(accountId);
    }

    @GetMapping("/transactions2/{customerId}/{productId}")
    public Flux<Transaction> getTransactions2(@PathVariable("customerId") String customerId, @PathVariable("productId") String productId){
        return service.getTransactions2(customerId, productId);
    }

    @GetMapping("accountnumber/{accountNumber}")
    public Mono<ResponseEntity<Account>> getByAccountNumber(@PathVariable("accountNumber") String accountNumber){
        return service.getByAccountNumber(accountNumber)
                .map(savedMessage -> ResponseEntity.ok(savedMessage))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/vip")
    public Mono<Account> createVip(@RequestBody Account account){
        return service.createVip(account);
    }

    @PostMapping("/pyme")
    public Mono<Account> createPyme(@RequestBody Account account){
        return service.createPyme(account);
    }

}
