package com.pgw.payflow.repository;

import com.pgw.payflow.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByPassportNumber(String passportNumber);

    boolean existsByPassportNumber(String passportNumber);
}
