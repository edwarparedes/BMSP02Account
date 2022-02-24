package com.nttdata.account.service;

import com.nttdata.account.entity.Account;
import com.nttdata.account.model.Customer;
import com.nttdata.account.model.Product;
import com.nttdata.account.model.Transaction;
import com.nttdata.account.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    IAccountRepository repository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Flux<Account> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Account> getAccountById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Account> save(Account account) {

        account.setCreationTime(LocalDateTime.now());
        Mono<Account> accountMono = repository.findByCustomerIdAndProductId(account.getCustomerId(), account.getProductId());
        Mono<Customer> customerMono = getCustomer(account.getCustomerId());
        Mono<Product> productMono = getProduct(account.getProductId());

        /*return productMono.flatMap(p -> {
            //accountMono.flatMap(a -> {
                if(p.getType().equalsIgnoreCase("Saving")){
                    //throw new RuntimeException("Cuenta de ahorros");
                    return repository.save(account);
                }else
                    throw new RuntimeException("Cuenta corriente");
                    //return null;
            //});
        });*/

        /*return customerMono.doOnNext(c1 -> {
            if(c1.getType().equalsIgnoreCase("Business")) {
                throw new RuntimeException("El cliente es Empresarial");
            }
        }).flatMap(c -> {
            return productMono.doOnNext(p -> {
                // Acá poner restricciones para producto (Opcional)
            }).flatMap(p2 -> {
                return accountMono.doOnNext(a -> {

                    getProduct(a.getProductId()).flatMap( ap -> {
                        throw new RuntimeException("El cliente ya cuenta con ese producto ...");
                    }).switchIfEmpty(repository.save(account));

                    //throw new RuntimeException("El cliente ya cuenta con ese producto ..."+ getProduct(a.getProductId()).doOnNext( ap -> ap.getType()));
                }).switchIfEmpty(repository.save(account));
            });
        });*/


        /*return customerMono.doOnNext(c1 -> {
            if(c1.getType().equalsIgnoreCase("Business")) {
                throw new RuntimeException("El cliente es Empresarial");
            }

        }).filter(fc -> {
            return fc.getType().equalsIgnoreCase("Personal");

        }).flatMap(c -> {
            return productMono.doOnNext(p -> {
                // Acá poner restricciones para producto (Opcional)
            }).flatMap(p2 -> {
                return accountMono.doOnNext(a -> {


                }).flatMap(am -> {
                    return getProduct(am.getProductId()).doOnNext( ap -> {
                        throw new RuntimeException("El cliente ya cuenta con ese producto de tipo " + ap.getType());
                    }).flatMap(pa -> {
                        return repository.save(account);
                    });

                }).switchIfEmpty(repository.save(account));
            });
        });*/

        return customerMono.doOnNext(c1 -> {
        }).flatMap(c -> {
            return productMono.doOnNext(p -> {
                // Acá poner restricciones para producto (Opcional)
                if(p.getType().equalsIgnoreCase("fixed term") && c.getType().equalsIgnoreCase("Business")) {
                    throw new RuntimeException("The business client cannot have a fixed-term account");
                }
                else if(p.getType().equalsIgnoreCase("saving") && c.getType().equalsIgnoreCase("Business")) {
                    throw new RuntimeException("The business customer cannot have a savings account");
                }
            }).flatMap(p2 -> {
                return accountMono.doOnNext(a -> {


                }).flatMap(am -> {
                    return getProduct(am.getProductId()).doOnNext( ap -> {
                        if(c.getType().equalsIgnoreCase("Personal"))
                            throw new RuntimeException("El cliente Personal ya cuenta con ese producto de tipo cuenta " + ap.getType());
                        else if(c.getType().equalsIgnoreCase("Business"))
                            throw new RuntimeException("El cliente Business ya cuenta con ese producto de tipo cuenta " + ap.getType());
                    }).flatMap(pa -> {
                        return repository.save(account);
                    });

                }).switchIfEmpty(repository.save(account));
            });
        });

    }

    @Override
    public Mono<Account> update(Account account) {
        return repository.save(account);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id).subscribe();
    }

    @Override
    public Mono<Customer> getCustomer(String customerId) {
        Mono<Customer> customerMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8001/customer/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class);
        return customerMono;
    }

    @Override
    public Mono<Product> getProduct(String productId) {
        Mono<Product> productMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8002/product/{productId}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Product.class);
        return productMono;
    }

    @Override
    public Mono<Account> checkBalance(String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }


    @Override
    public Flux<Transaction> getTransactions(String accountId) {
        Flux<Transaction> transactionFlux = webClientBuilder.build()
                .get()
                .uri("http://localhost:8004/transaction/gettransactionsbyaccount/{accountId}", accountId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Transaction.class);
        return transactionFlux;
    }

    @Override
    public Flux<Transaction> getTransactions2(String customerId, String productId) {
        return getAll().filter(a -> {
            return a.getCustomerId().equalsIgnoreCase(customerId);
        }).filter(b -> {
            return b.getProductId().equalsIgnoreCase(productId);
        }).flatMap(c -> {
            return getTransactions(c.getId());
        });
    }
}
