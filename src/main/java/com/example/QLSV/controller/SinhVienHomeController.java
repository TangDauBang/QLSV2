package com.example.QLSV.controller;

import com.example.QLSV.entity.SinhVien;
import com.example.QLSV.entity.DangKyHoc;
import com.example.QLSV.service.SinhVienService;
import com.example.QLSV.service.DangKyHocService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/sinhvien")
public class SinhVienHomeController {

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private DangKyHocService dangKyHocService;

    // 📌 Trang chủ sinh viên
    @GetMapping({"", "/", "/home"})
    public String home(Model model, HttpSession session) {
        // Lấy ID sinh viên từ session
        Long maSV = (Long) session.getAttribute("maSV");
        
        SinhVien sv = sinhVienService.getSinhVienById(maSV);
        
        if (sv != null) {
            model.addAttribute("sv", sv);
            
            // Lấy danh sách lớp đã đăng ký
            List<DangKyHoc> danhSachDangKy = dangKyHocService.getDangKyBySinhVien(maSV);
            model.addAttribute("danhSachDangKy", danhSachDangKy);
            model.addAttribute("totalLop", danhSachDangKy.size());
            
            // Tính số lớp có điểm và chưa có điểm
            long lopCoGiamDiem = danhSachDangKy.stream()
                .filter(dk -> dk.getDiem() != null && dk.getDiem().getDiemTongKet() != null)
                .count();
            model.addAttribute("lopCoGiamDiem", lopCoGiamDiem);
            model.addAttribute("lopChuaCoGiamDiem", danhSachDangKy.size() - lopCoGiamDiem);
            
            return "sinhvien-home";
        }
        
        return "redirect:/";
    }

    // 📌 Xem thông tin cá nhân
    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        Long maSV = (Long) session.getAttribute("maSV");
        
        SinhVien sv = sinhVienService.getSinhVienById(maSV);
        
        if (sv != null) {
            model.addAttribute("sv", sv);
            return "sinhvien-profile";
        }
        
        return "redirect:/sinhvien/home";
    }
}
