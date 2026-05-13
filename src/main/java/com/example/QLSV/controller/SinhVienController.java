package com.example.QLSV.controller;

import com.example.QLSV.entity.SinhVien;
import com.example.QLSV.service.SinhVienService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/sinhvien")
public class SinhVienController {

    @Autowired
    private SinhVienService sinhVienService;

    // 👉 Hiển thị danh sách sinh viên
    @GetMapping
    public String getAllSinhVien(Model model, @RequestParam(required = false) String search) {
        List<SinhVien> list;
        if (search != null && !search.trim().isEmpty()) {
            list = sinhVienService.searchByName(search.trim());
            model.addAttribute("search", search.trim());
        } else {
            list = sinhVienService.getAll();
            model.addAttribute("search", "");
        }
        model.addAttribute("listSinhVien", list);
        return "admin-sinhvien"; 
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "admin-sinhvien-add";
    }

    @PostMapping("/save")
    public String saveSinhVien(@ModelAttribute("sinhVien") SinhVien sv, Model model) {

        if (sv.getEmail() == null || !sv.getEmail().contains("@")) {
            model.addAttribute("error", "Email không hợp lệ!");
            model.addAttribute("sinhVien", sv);
            return "admin-sinhvien-add";
        }

        sinhVienService.createSinhVien(sv);
        return "redirect:/admin/sinhvien";
    }
    

    // 👉 Thêm sinh viên
    @PostMapping("/add")
    public String addSinhVien(@ModelAttribute SinhVien sinhVien) {
        sinhVienService.createSinhVien(sinhVien);
        return "redirect:/admin/sinhvien";
    }

    // 👉 Xóa sinh viên
    @GetMapping("/delete/{id}")
    public String deleteSinhVien(@PathVariable("id") Long id) {
        try {
            sinhVienService.deleteSinhVienById(id);
            System.out.println("Xóa OK: " + id);
        } catch (Exception e) {
            e.printStackTrace(); // 👈 QUAN TRỌNG
        }
        return "redirect:/admin/sinhvien";
    }

    @GetMapping("/edit/{id}")
    public String editSinhVien(@PathVariable Long id, Model model) {
        SinhVien sv = sinhVienService.getSinhVienById(id);
        model.addAttribute("sv", sv);
        return "admin-sinhvien-edit";
    }

    @PostMapping("/update")
    public String updateSinhVien(SinhVien sv) {
        sinhVienService.updateSinhVien(sv.getMaSV(), sv);
        return "redirect:/admin/sinhvien";
    }
}