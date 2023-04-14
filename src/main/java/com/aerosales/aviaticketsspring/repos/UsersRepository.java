package com.aerosales.aviaticketsspring.repos;

import com.aerosales.aviaticketsspring.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
}
