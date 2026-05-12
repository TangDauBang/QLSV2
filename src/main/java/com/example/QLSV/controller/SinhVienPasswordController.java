package com.example.QLSV.controller;

import com.example.QLSV.entity.SinhVien;
import com.example.QLSV.service.SinhVienService;
import com.example.QLSV.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sinhvien/password")
public class SinhVienPasswordController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private AccountService accountService;

    // 📌 Form đổi mật khẩu
    @GetMapping("/change")
    public String showChangePasswordForm(Model model) {
        Long maSV = 6L; // Lấy từ authentication
        
        SinhVien sv = sinhVienService.getSinhVienById(maSV);
        
        if (sv != null) {
            model.addAttribute("sv", sv);
            return "sinhvien-change-password";
        }
        
        return "redirect:/sinhvien/home";
    }

    // 📌 Xử lý đổi mật khẩu
    @PostMapping("/change")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
        try {
            Long maSV = 6L; // Lấy từ authentication
            
            // Kiểm tra mật khẩu mới và xác nhận có khớp không
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Mật khẩu mới không khớp với xác nhận!");
                return "sinhvien-change-password";
            }

            // Kiểm tra độ dài mật khẩu
            if (newPassword.length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                return "sinhvien-change-password";
            }

            // Kiểm tra mật khẩu cũ và mới khác nhau
            if (oldPassword.equals(newPassword)) {
                model.addAttribute("error", "Mật khẩu mới phải khác mật khẩu cũ!");
                return "sinhvien-change-password";
            }

            // Thay đổi mật khẩu
            // TODO: Implement changePasswordSinhVien in AccountService
            boolean success = accountService.changePassword(maSV, oldPassword, newPassword);
            
            if (success) {
                model.addAttribute("success", "Đổi mật khẩu thành công!");
                return "redirect:/sinhvien/home";
            } else {
                model.addAttribute("error", "Mật khẩu cũ không chính xác!");
                return "sinhvien-change-password";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "sinhvien-change-password";
        }
    }
}