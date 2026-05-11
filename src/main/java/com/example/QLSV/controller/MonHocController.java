package com.example.QLSV.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.QLSV.entity.MonHoc;
import com.example.QLSV.service.MonHocService;

import java.util.List;


@Controller
@RequestMapping("/admin/monhoc")
public class MonHocController {

    @Autowired
    private MonHocService monHocService;

    @GetMapping
    public String getAllMonHoc(Model model, @RequestParam(required = false) String search) {
        List<MonHoc> list;
        if (search != null && !search.trim().isEmpty()) {
            list = monHocService.searchByTenMon(search.trim());
            model.addAttribute("search", search.trim());
        } else {
            list = monHocService.getAll();
            model.addAttribute("search", "");
        }
        model.addAttribute("listMonHoc", list);
        return "admin-monhoc";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("monHoc", new MonHoc());
        return "admin-monhoc-add";
    }

    @PostMapping("/save")
    public String saveMonHoc(@ModelAttribute("monHoc") MonHoc mh, Model model) {

        if (mh.getTenMon() == null || mh.getTenMon().trim().isEmpty()) {
            model.addAttribute("error", "Tên môn học không được để trống!");
            model.addAttribute("monHoc", mh);
            return "admin-monhoc-add";
        }

        monHocService.createMonHoc(mh);
        return "redirect:/admin/monhoc";
    }

    @GetMapping("edit/{id}")
    public String editMonHoc(@PathVariable Long id, Model model) {
        MonHoc monhoc = monHocService.getMonHocById(id);
        model.addAttribute("mh", monhoc);
        return "admin-monhoc-edit";
    }

    @GetMapping("delete/{id}")
    public String deleteMonHoc(@PathVariable Long id) {
        monHocService.deleteMonHocById(id);
        return "redirect:/admin/monhoc";
    }

    @PostMapping("/update")
    public String updateMonHoc(@ModelAttribute("mh") MonHoc mh) {
        monHocService.updateMonHoc(mh.getMaMon(), mh);
        return "redirect:/admin/monhoc";
    }
}
