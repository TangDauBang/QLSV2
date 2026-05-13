package com.example.QLSV.service;

import com.example.QLSV.entity.Account;
import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.entity.SinhVien;
import com.example.QLSV.repository.AccountRepository;
import com.example.QLSV.repository.GiangVienRepository;
import com.example.QLSV.repository.SinhVienRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    // 📌 Lấy tài khoản theo ID
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    // 📌 Lấy tài khoản theo username
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }

    // 📌 Đổi mật khẩu (cho giảng viên)
    public boolean changePassword(Long maGV, String oldPassword, String newPassword) {
        GiangVien gv = giangVienRepository.findById(maGV).orElse(null);
        
        if (gv == null) {
            throw new RuntimeException("Giảng viên không tồn tại!");
        }

        Account account = gv.getAccount();
        
        if (account == null) {
            throw new RuntimeException("Tài khoản không tồn tại!");
        }

        // Kiểm tra mật khẩu cũ
        // TODO: Nên sử dụng password encoder thay vì so sánh plain text
        if (!account.getPassword().equals(oldPassword)) {
            return false;
        }

        // Cập nhật mật khẩu mới
        account.setPassword(newPassword);
        accountRepository.save(account);
        
        return true;
    }

    // 📌 Đổi mật khẩu (cho sinh viên)
    public boolean changePasswordSinhVien(Long maSV, String oldPassword, String newPassword) {
        // TODO: Implement similar logic for students
        SinhVien sv = sinhVienRepository.findById(maSV).orElse(null);
        if (sv == null) {
            throw new RuntimeException("Sinh viên không tồn tại!");
        }
        Account account = sv.getAccount();
        if (account == null) {
            throw new RuntimeException("Tài khoản không tồn tại!");
        }
        if (!account.getPassword().equals(oldPassword)) {
            return false;
        }
        account.setPassword(newPassword);
        accountRepository.save(account);
        return true;
    }

    // 📌 Tạo tài khoản
    public Account createAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password); // TODO: Nên hash password
        return accountRepository.save(account);
    }

    // 📌 Cập nhật tài khoản
    public Account updateAccount(Long id, Account newData) {
        Optional<Account> existing = accountRepository.findById(id);
        
        if (existing.isPresent()) {
            Account account = existing.get();
            if (newData.getPassword() != null) {
                account.setPassword(newData.getPassword());
            }
            if (newData.getRole() != null) {
                account.setRole(newData.getRole());
            }
            return accountRepository.save(account);
        }
        
        return null;
    }

    // 📌 Xóa tài khoản
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}