package com.aerosales.aviaticketsspring.repos;

import com.aerosales.aviaticketsspring.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
}
