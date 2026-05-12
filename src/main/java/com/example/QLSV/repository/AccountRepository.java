package com.example.QLSV.repository;

import com.example.QLSV.entity.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
     // 📌 Tìm tài khoản theo username
    Optional<Account> findByUsername(String username);
}