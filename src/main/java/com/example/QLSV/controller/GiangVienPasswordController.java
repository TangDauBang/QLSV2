package com.example.QLSV.controller;

// import com.example.QLSV.entity.Account;
import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.service.GiangVienService;
import com.example.QLSV.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/giangvien/password")
public class GiangVienPasswordController {

    @Autowired
    private GiangVienService giangVienService;

    @Autowired
    private AccountService accountService;

    // 📌 Form đổi mật khẩu
    @GetMapping("/change")
    public String showChangePasswordForm(Model model) {
        Long maGV = 2L; // Lấy từ authentication
        
        GiangVien gv = giangVienService.getGiangVienById(maGV);
        
        if (gv != null) {
            model.addAttribute("gv", gv);
            return "giangvien-change-password";
        }
        
        return "redirect:/giangvien/home";
    }

    // 📌 Xử lý đổi mật khẩu
    @PostMapping("/change")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
        try {
            Long maGV = 2L; // Lấy từ authentication
            
            // Kiểm tra mật khẩu mới và xác nhận có khớp không
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Mật khẩu mới không khớp với xác nhận!");
                return "giangvien-change-password";
            }

            // Kiểm tra độ dài mật khẩu
            if (newPassword.length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                return "giangvien-change-password";
            }

            // Thay đổi mật khẩu
            boolean success = accountService.changePassword(maGV, oldPassword, newPassword);
            
            if (success) {
                model.addAttribute("success", "Đổi mật khẩu thành công!");
                return "redirect:/giangvien/home";
            } else {
                model.addAttribute("error", "Mật khẩu cũ không chính xác!");
                return "giangvien-change-password";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "giangvien-change-password";
        }
    }
}