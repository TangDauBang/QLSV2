package com.example.QLSV.controller;

import com.example.QLSV.entity.LopMonHoc;
import com.example.QLSV.entity.MonHoc;
import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.service.LopMonHocService;
import com.example.QLSV.service.MonHocService;
import com.example.QLSV.service.GiangVienService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/lopmonhoc")
public class LopMonHocController {

    @Autowired
    private LopMonHocService lopMonHocService;
    
    @Autowired
    private MonHocService monHocService;
    
    @Autowired
    private GiangVienService giangVienService;

    // 📌 Danh sách lớp môn học + Tìm kiếm
    @GetMapping
    public String getAllLopMonHoc(Model model, 
                                  @RequestParam(required = false) String searchType,
                                  @RequestParam(required = false) String search) {
        List<LopMonHoc> list;
        
        if (search != null && !search.trim().isEmpty()) {
            search = search.trim();
            
            if ("monhoc".equals(searchType)) {
                list = lopMonHocService.searchByMonHoc(search);
                model.addAttribute("searchType", "monhoc");
            } else if ("giangvien".equals(searchType)) {
                list = lopMonHocService.searchByGiangVien(search);
                model.addAttribute("searchType", "giangvien");
            } else if ("khoa".equals(searchType)) {
                list = lopMonHocService.searchByKhoa(search);
                model.addAttribute("searchType", "khoa");
            } else {
                list = lopMonHocService.getAll();
                model.addAttribute("searchType", "");
            }
            
            model.addAttribute("search", search);
        } else {
            list = lopMonHocService.getAll();
            model.addAttribute("search", "");
            model.addAttribute("searchType", "");
        }
        
        model.addAttribute("listLopMonHoc", list);
        return "admin-lopmonhoc";
    }

    // 📌 Form thêm lớp môn học
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("lopMonHoc", new LopMonHoc());
        model.addAttribute("listMonHoc", monHocService.getAll());
        model.addAttribute("listGiangVien", giangVienService.getAll());
        return "admin-lopmonhoc-add";
    }

    // 📌 Lưu lớp môn học mới
    @PostMapping("/save")
    public String saveLopMonHoc(@ModelAttribute("lopMonHoc") LopMonHoc lop, Model model) {
        
        // Kiểm tra dữ liệu nhập
        if (lop.getMonHoc() == null || lop.getMonHoc().getMaMon() == null) {
            model.addAttribute("error", "Vui lòng chọn môn học!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getGiangVien() == null || lop.getGiangVien().getMaGV() == null) {
            model.addAttribute("error", "Vui lòng chọn giảng viên!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getNhom() == null || lop.getNhom().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập nhóm lớp!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getSiSoToiDa() == null || lop.getSiSoToiDa() <= 0) {
            model.addAttribute("error", "Sĩ số tối đa phải lớn hơn 0!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getNgayHoc() == null || lop.getNgayHoc().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng chọn ít nhất một ngày học!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getPhongHoc() == null || lop.getPhongHoc().trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập phòng học!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getTietBatDau() == null || lop.getTietBatDau() < 1 || lop.getTietBatDau() > 12) {
            model.addAttribute("error", "Tiết bắt đầu phải từ 1-12!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getTietKetThuc() == null || lop.getTietKetThuc() < 1 || lop.getTietKetThuc() > 12) {
            model.addAttribute("error", "Tiết kết thúc phải từ 1-12!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        if (lop.getTietBatDau() >= lop.getTietKetThuc()) {
            model.addAttribute("error", "Tiết bắt đầu phải nhỏ hơn tiết kết thúc!");
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
        
        try {
            // Lấy dữ liệu đủ từ database
            MonHoc monHoc = monHocService.getMonHocById(lop.getMonHoc().getMaMon());
            GiangVien giangVien = giangVienService.getGiangVienById(lop.getGiangVien().getMaGV());
            
            lop.setMonHoc(monHoc);
            lop.setGiangVien(giangVien);
            
            lopMonHocService.createLopMonHoc(lop);
            return "redirect:/admin/lopmonhoc";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("lopMonHoc", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-add";
        }
    }

    // 📌 Form sửa lớp môn học
    @GetMapping("/edit/{id}")
    public String editLopMonHoc(@PathVariable("id") Long id, Model model) {
        LopMonHoc lop = lopMonHocService.getLopMonHocById(id);
        
        if (lop != null) {
            model.addAttribute("lop", lop);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-edit";
        }
        
        return "redirect:/admin/lopmonhoc";
    }

    // 📌 Cập nhật lớp môn học
    @PostMapping("/update")
    public String updateLopMonHoc(@ModelAttribute("lop") LopMonHoc lop, Model model) {
        
        // Kiểm tra dữ liệu
        if (lop.getTietBatDau() >= lop.getTietKetThuc()) {
            LopMonHoc existing = lopMonHocService.getLopMonHocById(lop.getMaLopMonHoc());
            model.addAttribute("error", "Tiết bắt đầu phải nhỏ hơn tiết kết thúc!");
            model.addAttribute("lop", existing);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-edit";
        }
        
        try {
            // Lấy dữ liệu đủ từ database
            MonHoc monHoc = monHocService.getMonHocById(lop.getMonHoc().getMaMon());
            GiangVien giangVien = giangVienService.getGiangVienById(lop.getGiangVien().getMaGV());
            
            lop.setMonHoc(monHoc);
            lop.setGiangVien(giangVien);
            
            lopMonHocService.updateLopMonHoc(lop.getMaLopMonHoc(), lop);
            return "redirect:/admin/lopmonhoc";
        } catch (RuntimeException e) {
            LopMonHoc existing = lopMonHocService.getLopMonHocById(lop.getMaLopMonHoc());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("lop", existing);
            model.addAttribute("listMonHoc", monHocService.getAll());
            model.addAttribute("listGiangVien", giangVienService.getAll());
            return "admin-lopmonhoc-edit";
        }
    }

    // 📌 Xóa lớp môn học
    @GetMapping("/delete/{id}")
    public String deleteLopMonHoc(@PathVariable("id") Long id) {
        boolean deleted = lopMonHocService.deleteLopMonHoc(id);
        
        if (!deleted) {
            // Lớp có sinh viên đăng ký, không thể xóa
            // Có thể redirect và hiển thị thông báo
            // Tạm thời xóa bắt buộc
            lopMonHocService.deleteLopMonHocForce(id);
        }
        
        return "redirect:/admin/lopmonhoc";
    }

    // 📌 Xem danh sách sinh viên đăng ký
    @GetMapping("/students/{id}")
    public String getStudentsRegistered(@PathVariable("id") Long id, Model model) {
        LopMonHoc lop = lopMonHocService.getLopMonHocById(id);
        
        if (lop != null) {
            model.addAttribute("lop", lop);
            model.addAttribute("listStudents", lop.getDanhSachDangKy());
            model.addAttribute("registeredCount", lop.getDanhSachDangKy() != null ? lop.getDanhSachDangKy().size() : 0);
            model.addAttribute("capacity", lop.getSiSoToiDa());
            model.addAttribute("availableSeats", lopMonHocService.getAvailableSeats(id));
            return "admin-lopmonhoc-students";
        }
        
        return "redirect:/admin/lopmonhoc";
    }
}
