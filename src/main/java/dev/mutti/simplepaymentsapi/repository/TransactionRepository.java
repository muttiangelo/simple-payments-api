package dev.mutti.simplepaymentsapi.repository;

import dev.mutti.simplepaymentsapi.domain.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, Long> {
}
