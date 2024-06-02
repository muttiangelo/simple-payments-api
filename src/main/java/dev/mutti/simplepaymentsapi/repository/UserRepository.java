package dev.mutti.simplepaymentsapi.repository;

import dev.mutti.simplepaymentsapi.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long>, UserRepositoryCustom {
    User findByEmail(String email);
}