package com.nttdata.account.service;

import com.nttdata.account.entity.Account;
import com.nttdata.account.model.Customer;
import com.nttdata.account.model.Product;
import com.nttdata.account.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {

    Flux<Account> getAll();

    Mono<Account> getAccountById(String id);

    Mono<Account> save(Account account);

    Mono<Account> update(Account account);

    void delete(String id);

    Mono<Customer> getCustomer(String customerId);

    Mono<Product> getProduct(String productId);

    Mono<Account> checkBalance(String accountNumber);

    Flux<Transaction> getTransactions(String accountId);

    Flux<Transaction> getTransactions2(String customerId, String productId);

}