package com.example.QLSV.controller;

import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.entity.LopMonHoc;
import com.example.QLSV.service.GiangVienService;
import com.example.QLSV.service.LopMonHocService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/giangvien")
public class GiangVienHomeController {

    @Autowired
    private GiangVienService giangVienService;

    @Autowired
    private LopMonHocService lopMonHocService;

    // 📌 Trang chủ giảng viên
    @GetMapping({"", "/", "/home"})
    public String home(Model model, HttpSession session) {
        // Lấy ID giảng viên từ session
        Long maGV = (Long) session.getAttribute("maGV");
        
        GiangVien gv = giangVienService.getGiangVienById(maGV);
        
        if (gv != null) {
            model.addAttribute("gv", gv);
            
            // Lấy danh sách lớp của giảng viên
            List<LopMonHoc> danhSachLop = lopMonHocService.getClassesByTeacher(maGV);
            model.addAttribute("danhSachLop", danhSachLop);
            model.addAttribute("totalLop", danhSachLop.size());
            
            // Tính tổng số sinh viên
            long totalSinhVien = danhSachLop.stream()
                .mapToLong(lop -> lop.getDanhSachDangKy() != null ? lop.getDanhSachDangKy().size() : 0)
                .sum();
            model.addAttribute("totalSinhVien", totalSinhVien);
            
            return "giangvien-home";
        }
        
        return "redirect:/";
    }

    // 📌 Xem thông tin cá nhân
    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        Long maGV = (Long) session.getAttribute("maGV");
        
        GiangVien gv = giangVienService.getGiangVienById(maGV);
        
        if (gv != null) {
            model.addAttribute("gv", gv);
            return "giangvien-profile";
        }
        
        return "redirect:/giangvien/home";
    }
}