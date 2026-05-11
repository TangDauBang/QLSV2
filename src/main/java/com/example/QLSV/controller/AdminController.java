package com.example.QLSV.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.QLSV.service.*;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SinhVienService sinhVienService;
 
    @Autowired
    private GiangVienService giangVienService;
 
    @Autowired
    private MonHocService monHocService;
 
    @Autowired
    private LopMonHocService lopMonHocService;
 
    @Autowired
    private DangKyHocService dangKyHocService;

    @GetMapping({"", "/", "/home"})
    public String home(Model model) {

        // Lấy dữ liệu thống kê
        long totalSinhVien = sinhVienService.getAll().size();
        long totalGiangVien = giangVienService.getAll().size();
        long totalMonHoc = monHocService.getAll().size();
        long totalLopMonHoc = lopMonHocService.getAll().size();
        long totalDangKyHoc = dangKyHocService.getAll().size();
 
        // Thêm vào model
        model.addAttribute("totalSinhVien", totalSinhVien);
        model.addAttribute("totalGiangVien", totalGiangVien);
        model.addAttribute("totalMonHoc", totalMonHoc);
        model.addAttribute("totalLopMonHoc", totalLopMonHoc);
        model.addAttribute("totalDangKyHoc", totalDangKyHoc);

        return "admin-home"; // tên file html
    }
}
