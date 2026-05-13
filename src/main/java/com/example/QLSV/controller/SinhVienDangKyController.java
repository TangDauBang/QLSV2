package com.example.QLSV.controller;

import com.example.QLSV.entity.DangKyHoc;
import com.example.QLSV.entity.LopMonHoc;
// import com.example.QLSV.service.SinhVienService;
import com.example.QLSV.service.DangKyHocService;
import com.example.QLSV.service.LopMonHocService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/sinhvien/dangkyhoc")
public class SinhVienDangKyController {

    @Autowired
    private DangKyHocService dangKyHocService;

    // @Autowired
    // private SinhVienService sinhVienService;

    @Autowired
    private LopMonHocService lopMonHocService;

    // 📌 Danh sách lớp đã đăng ký + Tìm kiếm
    @GetMapping
    public String getDanhSachDangKy(Model model, 
                                     @RequestParam(required = false) String search,
                                     @RequestParam(required = false) String searchType,
                                     HttpSession session) {
        Long maSV = (Long) session.getAttribute("maSV");
        
        List<DangKyHoc> danhSachDangKy;
        
        if (search != null && !search.trim().isEmpty()) {
            search = search.trim();
            
            if ("monhoc".equals(searchType)) {
                danhSachDangKy = dangKyHocService.searchByMonHocName(search);
                model.addAttribute("searchType", "monhoc");
            } else if ("giangvien".equals(searchType)) {
                danhSachDangKy = dangKyHocService.searchByGiangVienName(search);
                model.addAttribute("searchType", "giangvien");
            } else {
                danhSachDangKy = dangKyHocService.searchByKeyword(search);
                model.addAttribute("searchType", "");
            }
            
            // Filter danh sách để chỉ hiển thị lớp của sinh viên hiện tại
            danhSachDangKy.removeIf(dk -> !dk.getSinhVien().getMaSV().equals(maSV));
            model.addAttribute("search", search);
        } else {
            danhSachDangKy = dangKyHocService.getDangKyBySinhVien(maSV);
            model.addAttribute("search", "");
            model.addAttribute("searchType", "");
        }
        
        model.addAttribute("danhSachDangKy", danhSachDangKy);
        return "sinhvien-dangkyhoc";
    }

    // 📌 Xem điểm chi tiết
    @GetMapping("/diem/{id}")
    public String viewDiem(@PathVariable("id") Long maDangKy, Model model, HttpSession session) {
        DangKyHoc dk = dangKyHocService.getDangKyHocById(maDangKy);
        
        if (dk != null) {
            // Kiểm tra sinh viên có quyền xem điểm này không
            Long maSV = (Long) session.getAttribute("maSV");
            if (!dk.getSinhVien().getMaSV().equals(maSV)) {
                return "redirect:/sinhvien/dangkyhoc";
            }
            
            model.addAttribute("dk", dk);
            if (dk.getDiem() != null) {
                model.addAttribute("diem", dk.getDiem());
            }
            return "sinhvien-diem";
        }
        
        return "redirect:/sinhvien/dangkyhoc";
    }

    // 📌 Danh sách lớp môn học chưa đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model,
                                   @RequestParam(required = false) String search,
                                   @RequestParam(required = false) String searchType,
                                   HttpSession session) {
        Long maSV = (Long) session.getAttribute("maSV");
        
        List<LopMonHoc> danhSachLop;
        
        if (search != null && !search.trim().isEmpty()) {
            search = search.trim();
            
            if ("monhoc".equals(searchType)) {
                danhSachLop = lopMonHocService.searchByMonHoc(search);
                model.addAttribute("searchType", "monhoc");
            } else if ("giangvien".equals(searchType)) {
                danhSachLop = lopMonHocService.searchByGiangVien(search);
                model.addAttribute("searchType", "giangvien");
            } else {
                danhSachLop = lopMonHocService.getAll();
                model.addAttribute("searchType", "");
            }
            
            model.addAttribute("search", search);
        } else {
            danhSachLop = lopMonHocService.getAll();
            model.addAttribute("search", "");
            model.addAttribute("searchType", "");
        }
        
        // Lọc bỏ các lớp đã đăng ký
        List<DangKyHoc> daDangKy = dangKyHocService.getDangKyBySinhVien(maSV);
        danhSachLop.removeIf(lop -> daDangKy.stream()
            .anyMatch(dk -> dk.getLopMonHoc().getMaLopMonHoc().equals(lop.getMaLopMonHoc())));
        
        model.addAttribute("danhSachLop", danhSachLop);
        return "sinhvien-dang-ky-form";
    }

    // 📌 Xử lý đăng ký lớp
    @PostMapping("/register")
    public String registerClass(@RequestParam("maLopMonHoc") Long maLopMonHoc, 
                                Model model, 
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        try {
            Long maSV = (Long) session.getAttribute("maSV");
            
            // Kiểm tra dữ liệu
            if (maLopMonHoc == null || maLopMonHoc <= 0) {
                model.addAttribute("error", "Vui lòng chọn lớp môn học!");
                return showRegisterForm(model, null, null, session);
            }
            
            // Tạo đăng ký
            dangKyHocService.createDangKyHoc(maSV, maLopMonHoc);
            
            redirectAttributes.addFlashAttribute("success", "Đăng ký lớp thành công!");
            return "redirect:/sinhvien/dangkyhoc";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return showRegisterForm(model, null, null, session);
        }
    }

    // 📌 Hủy đăng ký lớp
    @GetMapping("/cancel/{id}")
    public String cancelRegister(@PathVariable("id") Long maDangKy, 
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {
        DangKyHoc dk = dangKyHocService.getDangKyHocById(maDangKy);
        
        if (dk != null) {
            Long maSV = (Long) session.getAttribute("maSV");
            
            // Kiểm tra quyền
            if (dk.getSinhVien().getMaSV().equals(maSV)) {
                // Chỉ cho phép hủy nếu chưa có điểm
                if (dk.getDiem() == null || dk.getDiem().getDiemTongKet() == null) {
                    dangKyHocService.deleteDangKyHoc(maDangKy);
                    redirectAttributes.addFlashAttribute("success", "Hủy đăng ký thành công!");
                    return "redirect:/sinhvien/dangkyhoc";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Không thể hủy lớp đã có điểm!");
                    return "redirect:/sinhvien/dangkyhoc";
                }
            }
        }
        
        return "redirect:/sinhvien/dangkyhoc";
    }
}