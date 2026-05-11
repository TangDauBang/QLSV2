package com.example.QLSV.controller;

import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.service.GiangVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/giangvien")
public class GiangVienController {

    @Autowired
    private GiangVienService giangVienService;

    // 📌 Danh sách + Tìm kiếm
    @GetMapping
    public String getAllGiangVien(Model model, @RequestParam(required = false) String search) {
        List<GiangVien> list;
        if (search != null && !search.trim().isEmpty()) {
            list = giangVienService.searchByName(search.trim());
            model.addAttribute("search", search.trim());
        } else {
            list = giangVienService.getAll();
            model.addAttribute("search", "");
        }
        model.addAttribute("listGiangVien", list);
        return "admin-giangvien";
    }

    // 📌 Form thêm
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("giangVien", new GiangVien());
        return "admin-giangvien-add";
    }

    // 📌 Lưu (thêm)
    @PostMapping("/save")
    public String save(@ModelAttribute("giangVien") GiangVien gv, Model model) {

        if (gv.getEmail() == null || !gv.getEmail().contains("@")) {
            model.addAttribute("error", "Email không hợp lệ!");
            model.addAttribute("giangVien", gv);
            return "admin-giangvien-add";
        }

        giangVienService.createGiangVien(gv);
        return "redirect:/admin/giangvien";
    }

    // 📌 Form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        GiangVien gv = giangVienService.getGiangVienById(id);

        if (gv != null) {
            model.addAttribute("gv", gv);
            return "admin-giangvien-edit";
        }

        return "redirect:/admin/giangvien";
    }

    // 📌 Update
    @PostMapping("/update")
    public String update(@ModelAttribute("gv") GiangVien gv) {
        giangVienService.updateGiangVien(gv.getMaGV(), gv);
        return "redirect:/admin/giangvien";
    }

    // 📌 Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        giangVienService.deleteGiangVienById(id);
        return "redirect:/admin/giangvien";
    }
}
