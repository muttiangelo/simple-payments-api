package dev.mutti.simplepaymentsapi.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryCustom {

    long updateBalanceById(long id, double newBalance);
}
