package com.example.QLSV.controller;

import com.example.QLSV.entity.DangKyHoc;
// import com.example.QLSV.entity.SinhVien;
// import com.example.QLSV.entity.LopMonHoc;
import com.example.QLSV.service.DangKyHocService;
import com.example.QLSV.service.SinhVienService;
import com.example.QLSV.service.LopMonHocService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/admin/dangkyhoc")
public class DangKyHocController {

    @Autowired
    private DangKyHocService dangKyHocService;

    @Autowired
    private SinhVienService sinhVienService;

    @Autowired
    private LopMonHocService lopMonHocService;

    // 📌 Danh sách đăng ký + Tìm kiếm
    @GetMapping
    public String getAllDangKyHoc(Model model, 
                                   @RequestParam(required = false) String searchType,
                                   @RequestParam(required = false) String search) {
        List<DangKyHoc> list;
        
        if (search != null && !search.trim().isEmpty()) {
            search = search.trim();
            
            if ("sinhvien".equals(searchType)) {
                // Tìm kiếm theo tên sinh viên
                try {
                    Long maSV = Long.parseLong(search);
                    list = dangKyHocService.searchBySinhVienId(maSV);
                    model.addAttribute("searchType", "sinhvien");
                } catch (NumberFormatException e) {
                    list = dangKyHocService.searchBySinhVienName(search);
                    model.addAttribute("searchType", "sinhvien");
                }
            } else if ("monhoc".equals(searchType)) {
                list = dangKyHocService.searchByMonHocName(search);
                model.addAttribute("searchType", "monhoc");
            } else if ("giangvien".equals(searchType)) {
                list = dangKyHocService.searchByGiangVienName(search);
                model.addAttribute("searchType", "giangvien");
            } else {
                list = dangKyHocService.searchByKeyword(search);
                model.addAttribute("searchType", "");
            }
            
            model.addAttribute("search", search);
        } else {
            list = dangKyHocService.getAll();
            model.addAttribute("search", "");
            model.addAttribute("searchType", "");
        }
        
        model.addAttribute("listDangKyHoc", list);
        return "admin-dangkyhoc";
    }

    // 📌 Form thêm đăng ký
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("listSinhVien", sinhVienService.getAll());
        model.addAttribute("listLopMonHoc", lopMonHocService.getAll());
        return "admin-dangkyhoc-add";
    }

    // 📌 Lưu đăng ký mới
    @PostMapping("/save")
    public String saveDangKyHoc(@RequestParam("maSV") Long maSV,
                                @RequestParam("maLopMonHoc") Long maLopMonHoc,
                                Model model) {
        try {
            // Kiểm tra dữ liệu
            if (maSV == null || maSV <= 0) {
                model.addAttribute("error", "Vui lòng chọn sinh viên!");
                model.addAttribute("listSinhVien", sinhVienService.getAll());
                model.addAttribute("listLopMonHoc", lopMonHocService.getAll());
                return "admin-dangkyhoc-add";
            }

            if (maLopMonHoc == null || maLopMonHoc <= 0) {
                model.addAttribute("error", "Vui lòng chọn lớp môn học!");
                model.addAttribute("listSinhVien", sinhVienService.getAll());
                model.addAttribute("listLopMonHoc", lopMonHocService.getAll());
                return "admin-dangkyhoc-add";
            }

            // Tạo đăng ký
            dangKyHocService.createDangKyHoc(maSV, maLopMonHoc);
            return "redirect:/admin/dangkyhoc";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("listSinhVien", sinhVienService.getAll());
            model.addAttribute("listLopMonHoc", lopMonHocService.getAll());
            return "admin-dangkyhoc-add";
        }
    }

    // 📌 Form sửa đăng ký (đổi sang lớp khác)
    @GetMapping("/edit/{id}")
    public String editDangKyHoc(@PathVariable("id") Long id, Model model) {
        DangKyHoc dk = dangKyHocService.getDangKyHocById(id);
        
        if (dk != null) {
            model.addAttribute("dk", dk);
            model.addAttribute("listLopMonHoc", lopMonHocService.getAll());
            return "admin-dangkyhoc-edit";
        }
        
        return "redirect:/admin/dangkyhoc";
    }

    // 📌 Cập nhật đăng ký
    @PostMapping("/update")
    public String updateDangKyHoc(@PathVariable(required = false) Long id,
                                   @RequestParam("maDangKy") Long maDangKy,
                                   @RequestParam("maLopMonHoc") Long maLopMonHoc,
                                   Model model) {
        try {
            dangKyHocService.updateDangKyHoc(maDangKy, maLopMonHoc);
            return "redirect:/admin/dangkyhoc";
        } catch (RuntimeException e) {
            DangKyHoc dk = dangKyHocService.getDangKyHocById(maDangKy);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("dk", dk);
            model.addAttribute("listLopMonHoc", lopMonHocService.getAll());
            return "admin-dangkyhoc-edit";
        }
    }

    // 📌 Xóa đăng ký
    @GetMapping("/delete/{id}")
    public String deleteDangKyHoc(@PathVariable("id") Long id) {
        dangKyHocService.deleteDangKyHoc(id);
        return "redirect:/admin/dangkyhoc";
    }

    // 📌 Xem chi tiết / Nhập điểm
    @GetMapping("/diem/{id}")
    public String viewDiemForm(@PathVariable("id") Long id, Model model) {
        DangKyHoc dk = dangKyHocService.getDangKyHocById(id);

        if (dk != null) {
            model.addAttribute("dk", dk);

            // Nếu chưa có Diem, tạo mới
            if (dk.getDiem() == null) {
                model.addAttribute("diem", new com.example.QLSV.entity.Diem());
                model.addAttribute("isNew", true);
            } else {
                model.addAttribute("diem", dk.getDiem());
                model.addAttribute("isNew", false);
            }

            return "admin-dangkyhoc-diem";
        }

        return "redirect:/admin/dangkyhoc";
    }

    // 📌 Lưu điểm khi admin nhập trên admin-dangkyhoc-diem.html
    @Autowired
    private com.example.QLSV.service.DiemService diemService;
    @PostMapping("/diem/{id}/save")
    public String saveDiem(@PathVariable("id") Long id,
                             @RequestParam("maDangKy") Long maDangKy,
                             @RequestParam(name = "diemChuyenCan", required = false) Double diemChuyenCan,
                             @RequestParam(name = "diemGiuaKy", required = false) Double diemGiuaKy,
                             @RequestParam(name = "diemCuoiKy", required = false) Double diemCuoiKy,
                             Model model) {
        try {
            Long maDangKyToSave = (maDangKy != null ? maDangKy : id);

            // Kiểm tra tối thiểu: mã đăng ký phải tồn tại
            if (maDangKyToSave == null || maDangKyToSave <= 0) {
                model.addAttribute("error", "Mã đăng ký không hợp lệ!");
                return "redirect:/admin/dangkyhoc";
            }

            // Cho phép giá trị null, DiemService sẽ xử lý theo công thức
            diemService.saveDiem(maDangKyToSave, diemChuyenCan, diemGiuaKy, diemCuoiKy);

            return "redirect:/admin/dangkyhoc";
        } catch (RuntimeException e) {
            DangKyHoc dk = dangKyHocService.getDangKyHocById(maDangKy);
            model.addAttribute("dk", dk);
            model.addAttribute("error", e.getMessage());

            if (dk != null && dk.getDiem() != null) {
                model.addAttribute("diem", dk.getDiem());
                model.addAttribute("isNew", false);
            } else {
                model.addAttribute("diem", new com.example.QLSV.entity.Diem());
                model.addAttribute("isNew", true);
            }

            return "admin-dangkyhoc-diem";
        }
    }

}

