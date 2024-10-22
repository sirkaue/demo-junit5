package com.sirkaue.demojunit5.repository;

import com.sirkaue.demojunit5.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
