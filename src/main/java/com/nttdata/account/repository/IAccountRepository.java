package com.nttdata.account.repository;

import com.nttdata.account.entity.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IAccountRepository extends ReactiveMongoRepository<Account, String> {

    Mono<Account> findByCustomerIdAndProductId(String customerId, String productId);

    Mono<Account> findByAccountNumber(String accountNumber);
}

