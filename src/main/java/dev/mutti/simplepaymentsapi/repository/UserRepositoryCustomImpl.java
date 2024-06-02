package dev.mutti.simplepaymentsapi.repository;

import dev.mutti.simplepaymentsapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * Update user balance by userId. Requires a custom implementation because the default provided by MongoRepository
     * won't offer this signature
     *
     * @param userId     of the user to update
     * @param newBalance balance after the update
     * @return the number of matched documents
     */
    @Override
    public long updateBalanceById(long userId, double newBalance) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(userId));

        Update update = new Update();
        update.set("balance", newBalance);

        return mongoTemplate.updateFirst(query, update, User.class).getMatchedCount();
    }
}