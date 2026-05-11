package com.example.QLSV.service;

import com.example.QLSV.entity.LopMonHoc;
import com.example.QLSV.entity.MonHoc;
import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.repository.LopMonHocRepository;
import com.example.QLSV.repository.MonHocRepository;
import com.example.QLSV.repository.GiangVienRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LopMonHocService {

    @Autowired
    private LopMonHocRepository lopMonHocRepository;
    
    @Autowired
    private MonHocRepository monHocRepository;
    
    @Autowired
    private GiangVienRepository giangVienRepository;

    // 📌 Lấy tất cả lớp môn học
    public List<LopMonHoc> getAll() {
        return lopMonHocRepository.findAll();
    }

    // 📌 Lấy lớp theo ID
    public LopMonHoc getLopMonHocById(Long id) {
        return lopMonHocRepository.findById(id).orElse(null);
    }

    // 📌 Tìm kiếm theo môn học
    public List<LopMonHoc> searchByMonHoc(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return lopMonHocRepository.findByMonHocNameContainingIgnoreCase(keyword.trim());
    }

    // 📌 Tìm kiếm theo giảng viên
    public List<LopMonHoc> searchByGiangVien(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return lopMonHocRepository.findByGiangVienNameContainingIgnoreCase(keyword.trim());
    }

    // 📌 Tìm kiếm theo khoa
    public List<LopMonHoc> searchByKhoa(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return lopMonHocRepository.findByKhoaContainingIgnoreCase(keyword.trim());
    }

    // 📌 Tìm lớp theo ngày học
    public List<LopMonHoc> findByNgayHoc(String ngayHoc) {
        return lopMonHocRepository.findByNgayHoc(ngayHoc);
    }

    // 📌 Lấy tất cả lớp của một giảng viên
    public List<LopMonHoc> getClassesByTeacher(Long maGV) {
        GiangVien gv = giangVienRepository.findById(maGV).orElse(null);
        if (gv != null) {
            return lopMonHocRepository.findByGiangVien(gv);
        }
        return List.of();
    }

    // 📌 Lấy tất cả lớp của một môn học
    public List<LopMonHoc> getClassesBySubject(Long maMon) {
        MonHoc mh = monHocRepository.findById(maMon).orElse(null);
        if (mh != null) {
            return lopMonHocRepository.findByMonHoc(mh);
        }
        return List.of();
    }

    // 📌 Kiểm tra trùng lịch giảng viên
    public boolean hasTeacherScheduleConflict(Long maGV, String ngayHoc, Integer tietBatDau, Integer tietKetThuc) {
        List<LopMonHoc> conflicts = lopMonHocRepository.findConflictingClassesByTeacher(
            maGV, ngayHoc, tietBatDau, tietKetThuc
        );
        return !conflicts.isEmpty();
    }

    // 📌 Kiểm tra trùng lịch phòng học
    public boolean hasRoomScheduleConflict(String phongHoc, String ngayHoc, Integer tietBatDau, Integer tietKetThuc) {
        List<LopMonHoc> conflicts = lopMonHocRepository.findConflictingClassesByRoom(
            phongHoc, ngayHoc, tietBatDau, tietKetThuc
        );
        return !conflicts.isEmpty();
    }

    // 📌 Kiểm tra trùng lịch giảng viên (khi edit, bỏ qua chính nó)
    public boolean hasTeacherScheduleConflictExcludingThis(
            Long maGV, String ngayHoc, Integer tietBatDau, Integer tietKetThuc, Long maLopMonHoc) {
        List<LopMonHoc> conflicts = lopMonHocRepository.findConflictingTeacherClassesExcludingThis(
            maGV, ngayHoc, tietBatDau, tietKetThuc, maLopMonHoc
        );
        return !conflicts.isEmpty();
    }

    // 📌 Kiểm tra trùng lịch phòng (khi edit)
    public boolean hasRoomScheduleConflictExcludingThis(
            String phongHoc, String ngayHoc, Integer tietBatDau, Integer tietKetThuc, Long maLopMonHoc) {
        List<LopMonHoc> conflicts = lopMonHocRepository.findConflictingRoomClassesExcludingThis(
            phongHoc, ngayHoc, tietBatDau, tietKetThuc, maLopMonHoc
        );
        return !conflicts.isEmpty();
    }

    // 📌 Tạo lớp môn học mới (với kiểm tra trùng lịch)
    public LopMonHoc createLopMonHoc(LopMonHoc lop) {
        // Kiểm tra trùng lịch giảng viên
        if (hasTeacherScheduleConflict(lop.getGiangVien().getMaGV(), lop.getNgayHoc(), 
                                       lop.getTietBatDau(), lop.getTietKetThuc())) {
            throw new RuntimeException("Giảng viên đã có lớp khác cùng thời gian!");
        }
        
        // Kiểm tra trùng lịch phòng
        if (hasRoomScheduleConflict(lop.getPhongHoc(), lop.getNgayHoc(), 
                                    lop.getTietBatDau(), lop.getTietKetThuc())) {
            throw new RuntimeException("Phòng học đã được sử dụng cùng thời gian!");
        }
        
        return lopMonHocRepository.save(lop);
    }

    // 📌 Cập nhật lớp môn học
    public LopMonHoc updateLopMonHoc(Long id, LopMonHoc newData) {
        Optional<LopMonHoc> existing = lopMonHocRepository.findById(id);
        
        if (existing.isPresent()) {
            LopMonHoc lop = existing.get();
            
            // Kiểm tra trùng lịch giảng viên (bỏ qua chính nó)
            if (!lop.getGiangVien().getMaGV().equals(newData.getGiangVien().getMaGV()) ||
                !lop.getNgayHoc().equals(newData.getNgayHoc()) ||
                !lop.getTietBatDau().equals(newData.getTietBatDau()) ||
                !lop.getTietKetThuc().equals(newData.getTietKetThuc())) {
                
                if (hasTeacherScheduleConflictExcludingThis(
                        newData.getGiangVien().getMaGV(), newData.getNgayHoc(),
                        newData.getTietBatDau(), newData.getTietKetThuc(), id)) {
                    throw new RuntimeException("Giảng viên đã có lớp khác cùng thời gian!");
                }
            }
            
            // Kiểm tra trùng lịch phòng (bỏ qua chính nó)
            if (!lop.getPhongHoc().equals(newData.getPhongHoc()) ||
                !lop.getNgayHoc().equals(newData.getNgayHoc()) ||
                !lop.getTietBatDau().equals(newData.getTietBatDau()) ||
                !lop.getTietKetThuc().equals(newData.getTietKetThuc())) {
                
                if (hasRoomScheduleConflictExcludingThis(
                        newData.getPhongHoc(), newData.getNgayHoc(),
                        newData.getTietBatDau(), newData.getTietKetThuc(), id)) {
                    throw new RuntimeException("Phòng học đã được sử dụng cùng thời gian!");
                }
            }
            
            // Cập nhật dữ liệu
            lop.setMonHoc(newData.getMonHoc());
            lop.setGiangVien(newData.getGiangVien());
            lop.setNhom(newData.getNhom());
            lop.setSiSoToiDa(newData.getSiSoToiDa());
            lop.setNgayHoc(newData.getNgayHoc());
            lop.setPhongHoc(newData.getPhongHoc());
            lop.setTietBatDau(newData.getTietBatDau());
            lop.setTietKetThuc(newData.getTietKetThuc());
            
            return lopMonHocRepository.save(lop);
        }
        
        return null;
    }

    // 📌 Xóa lớp môn học (kiểm tra có sinh viên đăng ký không)
    public boolean deleteLopMonHoc(Long id) {
        Optional<LopMonHoc> lop = lopMonHocRepository.findById(id);
        
        if (lop.isPresent()) {
            // Kiểm tra xem có sinh viên đăng ký không
            if (lop.get().getDanhSachDangKy() != null && !lop.get().getDanhSachDangKy().isEmpty()) {
                return false; // Không thể xóa nếu có sinh viên đăng ký
            }
            
            lopMonHocRepository.deleteById(id);
            return true;
        }
        
        return false;
    }

    // 📌 Xóa lớp môn học (bắt buộc - không kiểm tra sinh viên)
    public void deleteLopMonHocForce(Long id) {
        lopMonHocRepository.deleteById(id);
    }

    // 📌 Lấy số lượng sinh viên đã đăng ký
    public Integer getRegisteredStudentCount(Long maLopMonHoc) {
        LopMonHoc lop = lopMonHocRepository.findById(maLopMonHoc).orElse(null);
        if (lop != null && lop.getDanhSachDangKy() != null) {
            return lop.getDanhSachDangKy().size();
        }
        return 0;
    }

    // 📌 Lấy số lượng chỗ còn trống
    public Integer getAvailableSeats(Long maLopMonHoc) {
        LopMonHoc lop = lopMonHocRepository.findById(maLopMonHoc).orElse(null);
        if (lop != null) {
            Integer registered = getRegisteredStudentCount(maLopMonHoc);
            return lop.getSiSoToiDa() - registered;
        }
        return 0;
    }

    // 📌 Kiểm tra lớp còn chỗ không
    public boolean hasAvailableSeats(Long maLopMonHoc) {
        return getAvailableSeats(maLopMonHoc) > 0;
    }
}