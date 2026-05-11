package com.example.QLSV.service;

import com.example.QLSV.entity.DangKyHoc;
import com.example.QLSV.entity.SinhVien;
import com.example.QLSV.entity.LopMonHoc;
// import com.example.QLSV.entity.Diem;
import com.example.QLSV.enums.TrangThai;
import com.example.QLSV.repository.DangKyHocRepository;
import com.example.QLSV.repository.SinhVienRepository;
import com.example.QLSV.repository.LopMonHocRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
// import java.util.Optional;

@Service
public class DangKyHocService {

    @Autowired
    private DangKyHocRepository dangKyHocRepository;
    
    @Autowired
    private SinhVienRepository sinhVienRepository;
    
    @Autowired
    private LopMonHocRepository lopMonHocRepository;

    // 📌 Lấy tất cả đăng ký
    public List<DangKyHoc> getAll() {
        return dangKyHocRepository.findAll();
    }

    // 📌 Lấy đăng ký theo ID
    public DangKyHoc getDangKyHocById(Long id) {
        return dangKyHocRepository.findById(id).orElse(null);
    }

    // 📌 Tìm kiếm theo tên sinh viên
    public List<DangKyHoc> searchBySinhVienName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return dangKyHocRepository.findBySinhVienName(keyword.trim());
    }

    // 📌 Tìm kiếm theo mã sinh viên
    public List<DangKyHoc> searchBySinhVienId(Long maSV) {
        return dangKyHocRepository.findBySinhVienId(maSV);
    }

    // 📌 Tìm kiếm theo tên môn học
    public List<DangKyHoc> searchByMonHocName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return dangKyHocRepository.findByMonHocName(keyword.trim());
    }

    // 📌 Tìm kiếm theo tên giảng viên
    public List<DangKyHoc> searchByGiangVienName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return dangKyHocRepository.findByGiangVienName(keyword.trim());
    }

    // 📌 Tìm kiếm kết hợp
    public List<DangKyHoc> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return dangKyHocRepository.findByKeyword(keyword.trim());
    }

    // 📌 Lấy tất cả đăng ký của một sinh viên
    public List<DangKyHoc> getDangKyBySinhVien(Long maSV) {
        SinhVien sv = sinhVienRepository.findById(maSV).orElse(null);
        if (sv != null) {
            return dangKyHocRepository.findBySinhVien(sv);
        }
        return List.of();
    }

    // 📌 Lấy tất cả đăng ký của một lớp
    public List<DangKyHoc> getDangKyByLopMonHoc(Long maLopMonHoc) {
        LopMonHoc lop = lopMonHocRepository.findById(maLopMonHoc).orElse(null);
        if (lop != null) {
            return dangKyHocRepository.findByLopMonHoc(lop);
        }
        return List.of();
    }

    // 📌 Kiểm tra sinh viên đã đăng ký lớp này chưa
    public boolean isStudentRegistered(Long maSV, Long maLopMonHoc) {
        return dangKyHocRepository.findByStudentAndClass(maSV, maLopMonHoc).isPresent();
    }

    // 📌 Tạo đăng ký mới (với kiểm tra)
    public DangKyHoc createDangKyHoc(Long maSV, Long maLopMonHoc) {
        // Kiểm tra sinh viên tồn tại
        SinhVien sv = sinhVienRepository.findById(maSV).orElse(null);
        if (sv == null) {
            throw new RuntimeException("Sinh viên không tồn tại!");
        }

        // Kiểm tra sinh viên đang học
        if (sv.getTrangThai() != TrangThai.DANG_HOC) {
            throw new RuntimeException("Sinh viên không ở tình trạng đang học!");
        }

        // Kiểm tra lớp tồn tại
        LopMonHoc lop = lopMonHocRepository.findById(maLopMonHoc).orElse(null);
        if (lop == null) {
            throw new RuntimeException("Lớp môn học không tồn tại!");
        }

        // Kiểm tra sinh viên chưa đăng ký lớp này
        if (isStudentRegistered(maSV, maLopMonHoc)) {
            throw new RuntimeException("Sinh viên đã đăng ký lớp này!");
        }

        // Kiểm tra lớp còn chỗ
        int registeredCount = (int) dangKyHocRepository.countByLopMonHoc(lop);
        if (registeredCount >= lop.getSiSoToiDa()) {
            throw new RuntimeException("Lớp đã đầy sĩ số!");
        }

        // Kiểm tra trùng lịch học
        if (hasScheduleConflict(sv, lop)) {
            throw new RuntimeException("Sinh viên có trùng lịch học với lớp khác!");
        }

        // Tạo đăng ký
        DangKyHoc dk = new DangKyHoc();
        dk.setSinhVien(sv);
        dk.setLopMonHoc(lop);
        dk.setNgayDangKy(LocalDateTime.now());
        
        return dangKyHocRepository.save(dk);
    }

    // 📌 Kiểm tra trùng lịch học
    private boolean hasScheduleConflict(SinhVien sv, LopMonHoc lopMoi) {
        List<DangKyHoc> danhSachDangKy = dangKyHocRepository.findBySinhVien(sv);
        
        for (DangKyHoc dk : danhSachDangKy) {
            LopMonHoc lopCu = dk.getLopMonHoc();
            
            // Kiểm tra cùng ngày
            if (lopCu.getNgayHoc().equals(lopMoi.getNgayHoc())) {
                // Kiểm tra trùng tiết
                if (!(lopCu.getTietKetThuc() < lopMoi.getTietBatDau() || 
                      lopCu.getTietBatDau() > lopMoi.getTietKetThuc())) {
                    return true; // Có trùng lịch
                }
            }
        }
        
        return false; // Không trùng lịch
    }

    // 📌 Cập nhật đăng ký (đổi sang lớp khác)
    public DangKyHoc updateDangKyHoc(Long maDangKy, Long maLopMonHocMoi) {
        DangKyHoc dk = dangKyHocRepository.findById(maDangKy).orElse(null);
        if (dk == null) {
            throw new RuntimeException("Đăng ký không tồn tại!");
        }

        LopMonHoc lopMoi = lopMonHocRepository.findById(maLopMonHocMoi).orElse(null);
        if (lopMoi == null) {
            throw new RuntimeException("Lớp môn học không tồn tại!");
        }

        // Kiểm tra không đăng ký lớp cũ lại
        if (dk.getLopMonHoc().getMaLopMonHoc().equals(maLopMonHocMoi)) {
            throw new RuntimeException("Lớp mới phải khác lớp cũ!");
        }

        // Kiểm tra sinh viên chưa đăng ký lớp mới
        if (isStudentRegistered(dk.getSinhVien().getMaSV(), maLopMonHocMoi)) {
            throw new RuntimeException("Sinh viên đã đăng ký lớp này!");
        }

        // Kiểm tra lớp còn chỗ
        int registeredCount = (int) dangKyHocRepository.countByLopMonHoc(lopMoi);
        if (registeredCount >= lopMoi.getSiSoToiDa()) {
            throw new RuntimeException("Lớp đã đầy sĩ số!");
        }

        // Kiểm tra trùng lịch
        if (hasScheduleConflict(dk.getSinhVien(), lopMoi)) {
            throw new RuntimeException("Sinh viên có trùng lịch học!");
        }

        // Cập nhật
        dk.setLopMonHoc(lopMoi);
        return dangKyHocRepository.save(dk);
    }

    // 📌 Xóa đăng ký
    public boolean deleteDangKyHoc(Long maDangKy) {
        if (dangKyHocRepository.existsById(maDangKy)) {
            dangKyHocRepository.deleteById(maDangKy);
            return true;
        }
        return false;
    }

    // 📌 Lấy đăng ký có điểm
    public List<DangKyHoc> getDangKyWithScores() {
        return dangKyHocRepository.findAllWithScores();
    }

    // 📌 Lấy đăng ký chưa có điểm
    public List<DangKyHoc> getDangKyWithoutScores() {
        return dangKyHocRepository.findAllWithoutScores();
    }

    // 📌 Lấy số lượng đăng ký của lớp
    public long getRegisteredCount(Long maLopMonHoc) {
        LopMonHoc lop = lopMonHocRepository.findById(maLopMonHoc).orElse(null);
        if (lop != null) {
            return dangKyHocRepository.countByLopMonHoc(lop);
        }
        return 0;
    }

    // 📌 Kiểm tra sinh viên có điểm chưa
    public boolean hasScore(Long maDangKy) {
        DangKyHoc dk = dangKyHocRepository.findById(maDangKy).orElse(null);
        if (dk != null) {
            return dk.getDiem() != null && dk.getDiem().getDiemTongKet() != null;
        }
        return false;
    }
}
