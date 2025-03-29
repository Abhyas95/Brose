package com.backend.repository.MongoRepository;



import com.backend.Model.MongoDbTable.UserAccounts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserAccounts, String> {
    // You can add custom query methods if needed
    Optional<UserAccounts> findByUserName(String userName);

}

