package com.example.QLSV.controller;

import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.entity.LopMonHoc;
import com.example.QLSV.entity.DangKyHoc;
import com.example.QLSV.service.GiangVienService;
import com.example.QLSV.service.LopMonHocService;
import com.example.QLSV.service.DangKyHocService;
import com.example.QLSV.service.DiemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/giangvien/lopmonhoc")
public class GiangVienLopMonHocController {

    @Autowired
    private GiangVienService giangVienService;

    @Autowired
    private LopMonHocService lopMonHocService;

    @Autowired
    private DangKyHocService dangKyHocService;

    @Autowired
    private DiemService diemService;

    // 📌 Danh sách lớp của giảng viên
    @GetMapping
    public String getAllClassesByTeacher(Model model, HttpSession session) {
        Long maGV = (Long) session.getAttribute("maGV");
        
        GiangVien gv = giangVienService.getGiangVienById(maGV);
        
        if (gv != null) {
            List<LopMonHoc> danhSachLop = lopMonHocService.getClassesByTeacher(maGV);
            model.addAttribute("gv", gv);
            model.addAttribute("danhSachLop", danhSachLop);
            return "giangvien-lopmonhoc";
        }
        
        return "redirect:/giangvien/home";
    }

    // 📌 Xem danh sách sinh viên và nhập điểm
    @GetMapping("/{id}/students")
    public String viewStudentsAndScores(@PathVariable("id") Long maLopMonHoc, Model model, HttpSession session) {
        Long maGV = (Long) session.getAttribute("maGV");
        
        LopMonHoc lop = lopMonHocService.getLopMonHocById(maLopMonHoc);
        
        // Kiểm tra lớp có thuộc về giảng viên này không
        if (lop != null && lop.getGiangVien().getMaGV().equals(maGV)) {
            model.addAttribute("lop", lop);
            model.addAttribute("danhSachDangKy", lop.getDanhSachDangKy());
            model.addAttribute("totalStudents", lop.getDanhSachDangKy() != null ? lop.getDanhSachDangKy().size() : 0);
            return "giangvien-lopmonhoc-students";
        }
        
        return "redirect:/giangvien/lopmonhoc";
    }

    // 📌 Form nhập điểm
    @GetMapping("/{id}/diem/{maDangKy}")
    public String viewDiemForm(@PathVariable("id") Long maLopMonHoc,
                               @PathVariable("maDangKy") Long maDangKy,
                               Model model,
                               HttpSession session) {
        Long maGV = (Long) session.getAttribute("maGV");
        
        DangKyHoc dk = dangKyHocService.getDangKyHocById(maDangKy);
        LopMonHoc lop = lopMonHocService.getLopMonHocById(maLopMonHoc);
        
        // Kiểm tra quyền truy cập (lớp phải thuộc giảng viên này)
        if (dk != null && lop != null && lop.getGiangVien().getMaGV().equals(maGV) && 
            dk.getLopMonHoc().getMaLopMonHoc().equals(maLopMonHoc)) {
            
            model.addAttribute("dk", dk);
            model.addAttribute("lop", lop);
            
            // Nếu chưa có Diem, tạo mới
            if (dk.getDiem() == null) {
                model.addAttribute("diem", new com.example.QLSV.entity.Diem());
                model.addAttribute("isNew", true);
            } else {
                model.addAttribute("diem", dk.getDiem());
                model.addAttribute("isNew", false);
            }
            
            return "giangvien-diem";
        }
        
        return "redirect:/giangvien/lopmonhoc";
    }

    // 📌 Lưu điểm
    @PostMapping("/{id}/diem/{maDangKy}/save")
    public String saveDiem(@PathVariable("id") Long maLopMonHoc,
                          @PathVariable("maDangKy") Long maDangKy,
                          @RequestParam(name = "diemChuyenCan", required = false) Double diemChuyenCan,
                          @RequestParam(name = "diemGiuaKy", required = false) Double diemGiuaKy,
                          @RequestParam(name = "diemCuoiKy", required = false) Double diemCuoiKy,
                          Model model,
                          HttpSession session) {
        try {
            Long maGV = (Long) session.getAttribute("maGV");
            
            DangKyHoc dk = dangKyHocService.getDangKyHocById(maDangKy);
            LopMonHoc lop = lopMonHocService.getLopMonHocById(maLopMonHoc);
            
            // Kiểm tra quyền truy cập
            if (dk == null || lop == null || !lop.getGiangVien().getMaGV().equals(maGV) ||
                !dk.getLopMonHoc().getMaLopMonHoc().equals(maLopMonHoc)) {
                model.addAttribute("error", "Bạn không có quyền truy cập!");
                return "redirect:/giangvien/lopmonhoc";
            }

            // Lưu điểm
            diemService.saveDiem(maDangKy, diemChuyenCan, diemGiuaKy, diemCuoiKy);

            return "redirect:/giangvien/lopmonhoc/" + maLopMonHoc + "/students";
        } catch (RuntimeException e) {
            DangKyHoc dk = dangKyHocService.getDangKyHocById(maDangKy);
            LopMonHoc lop = lopMonHocService.getLopMonHocById(maLopMonHoc);
            
            model.addAttribute("lop", lop);
            model.addAttribute("dk", dk);
            model.addAttribute("error", e.getMessage());

            if (dk != null && dk.getDiem() != null) {
                model.addAttribute("diem", dk.getDiem());
                model.addAttribute("isNew", false);
            } else {
                model.addAttribute("diem", new com.example.QLSV.entity.Diem());
                model.addAttribute("isNew", true);
            }

            return "giangvien-diem";
        }
    }
}