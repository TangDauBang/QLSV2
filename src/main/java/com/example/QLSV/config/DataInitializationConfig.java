package com.example.QLSV.config;

import com.example.QLSV.entity.Account;
import com.example.QLSV.enums.Role;
import com.example.QLSV.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DataInitializationConfig implements ApplicationRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 🔧 Khởi tạo tài khoản admin mặc định
        initializeAdminAccount();
        
        // 🌐 Mở Chrome tự động
        openBrowserAutomatically();
    }

    /**
     * Khởi tạo tài khoản admin nếu chưa tồn tại
     */
    private void initializeAdminAccount() {
        // Kiểm tra xem tài khoản admin đã tồn tại chưa
        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account adminAccount = new Account();
            adminAccount.setUsername("admin");
            adminAccount.setPassword("123456"); // ⚠️ Lưu ý: Nên sử dụng mã hóa mật khẩu trong production
            adminAccount.setRole(Role.ADMIN);
            
            accountRepository.save(adminAccount);
            System.out.println("✅ Tài khoản admin đã được khởi tạo!");
            System.out.println("   👤 Username: admin");
            System.out.println("   🔐 Password: 123456");
        } else {
            System.out.println("ℹ️  Tài khoản admin đã tồn tại!");
        }
    }

    /**
     * Mở Chrome tự động khi ứng dụng khởi động
     */
    private void openBrowserAutomatically() {
        try {
            // URL của ứng dụng
            String url = "http://localhost:8081";
            
            // Xác định hệ điều hành
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Windows
                String[] cmd = {
                    "cmd",
                    "/c",
                    "start chrome " + url
                };
                Runtime.getRuntime().exec(cmd);
                System.out.println("🌐 Đang mở Chrome trên Windows...");
            } else if (os.contains("mac")) {
                // macOS
                String[] cmd = {
                    "open",
                    "-a",
                    "Google Chrome",
                    url
                };
                Runtime.getRuntime().exec(cmd);
                System.out.println("🌐 Đang mở Chrome trên macOS...");
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                Runtime.getRuntime().exec(new String[] {
                    "bash",
                    "-c",
                    "google-chrome " + url
                });
                System.out.println("🌐 Đang mở Chrome trên Linux...");
            }
        } catch (IOException e) {
            System.out.println("⚠️  Không thể mở Chrome tự động!");
            System.out.println("   Vui lòng truy cập: http://localhost:8081");
            System.out.println("   Nguyên nhân: " + e.getMessage());
        }
    }
}
