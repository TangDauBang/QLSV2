package com.example.QLSV.controller;

import com.example.QLSV.entity.Account;
// import com.example.QLSV.entity.SinhVien;
// import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.service.AccountService;
// import com.example.QLSV.service.SinhVienService;
// import com.example.QLSV.service.GiangVienService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private AccountService accountService;

    // @Autowired
    // private SinhVienService sinhVienService;

    // @Autowired
    // private GiangVienService giangVienService;

    // 📌 Trang đăng nhập
    @GetMapping
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        // Nếu đã đăng nhập, redirect sang trang phù hợp
        if (session.getAttribute("accountId") != null) {
            String role = (String) session.getAttribute("role");
            if ("ADMIN".equals(role)) {
                return "redirect:/admin";
            } else if ("GIANG_VIEN".equals(role)) {
                return "redirect:/giangvien/home";
            } else if ("SINH_VIEN".equals(role)) {
                return "redirect:/sinhvien/home";
            }
        }
        return "login";
    }

    // 📌 Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {
        try {
            // Tìm account theo username
            Account account = accountService.getAccountByUsername(username);

            if (account == null) {
                model.addAttribute("error", "Tài khoản không tồn tại!");
                return "login";
            }

            // Kiểm tra mật khẩu (tạm thời so sánh plain text - nên dùng encryption)
            if (!account.getPassword().equals(password)) {
                model.addAttribute("error", "Mật khẩu không chính xác!");
                return "login";
            }

            // Kiểm tra role
            if (account.getRole() == null) {
                model.addAttribute("error", "Tài khoản không có quyền truy cập!");
                return "login";
            }

            // Lưu session
            session.setAttribute("accountId", account.getId());
            session.setAttribute("username", account.getUsername());
            session.setAttribute("role", account.getRole().toString());

            // Redirect dựa theo role
            switch (account.getRole().toString()) {
                case "ADMIN":
                    return "redirect:/admin";

                case "GIANG_VIEN":
                    // Lấy thông tin giảng viên
                    if (account.getGiangVien() != null) {
                        Long maGV = account.getGiangVien().getMaGV();
                        session.setAttribute("maGV", maGV);
                        session.setAttribute("gvName", account.getGiangVien().getHoTen());
                    }
                    return "redirect:/giangvien/home";

                case "SINH_VIEN":
                    // Lấy thông tin sinh viên
                    if (account.getSinhVien() != null) {
                        Long maSV = account.getSinhVien().getMaSV();
                        session.setAttribute("maSV", maSV);
                        session.setAttribute("svName", account.getSinhVien().getHoTen());
                    }
                    return "redirect:/sinhvien/home";

                default:
                    model.addAttribute("error", "Role không hợp lệ!");
                    return "login";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Lỗi đăng nhập: " + e.getMessage());
            return "login";
        }
    }

    // 📌 Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 📌 Trang báo lỗi - không có quyền truy cập
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}