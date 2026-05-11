package com.example.QLSV.repository;

import com.example.QLSV.entity.DangKyHoc;
import com.example.QLSV.entity.SinhVien;
import com.example.QLSV.entity.LopMonHoc;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DangKyHocRepository extends JpaRepository<DangKyHoc, Long> {
    
    // 📌 Tìm tất cả đăng ký của một sinh viên
    List<DangKyHoc> findBySinhVien(SinhVien sinhVien);
    
    // 📌 Tìm tất cả đăng ký của một lớp
    List<DangKyHoc> findByLopMonHoc(LopMonHoc lopMonHoc);
    
    // 📌 Kiểm tra sinh viên đã đăng ký lớp này chưa
    @Query("SELECT dk FROM DangKyHoc dk WHERE dk.sinhVien.maSV = :maSV AND dk.lopMonHoc.maLopMonHoc = :maLopMonHoc")
    Optional<DangKyHoc> findByStudentAndClass(
        @Param("maSV") Long maSV,
        @Param("maLopMonHoc") Long maLopMonHoc
    );
    
    // 📌 Tìm kiếm theo tên sinh viên
    @Query("SELECT dk FROM DangKyHoc dk WHERE UPPER(dk.sinhVien.hoTen) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<DangKyHoc> findBySinhVienName(@Param("keyword") String keyword);
    
    // 📌 Tìm kiếm theo mã sinh viên
    @Query("SELECT dk FROM DangKyHoc dk WHERE dk.sinhVien.maSV = :maSV")
    List<DangKyHoc> findBySinhVienId(@Param("maSV") Long maSV);
    
    // 📌 Tìm kiếm theo tên môn học
    @Query("SELECT dk FROM DangKyHoc dk WHERE UPPER(dk.lopMonHoc.monHoc.tenMon) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<DangKyHoc> findByMonHocName(@Param("keyword") String keyword);
    
    // 📌 Tìm kiếm theo tên giảng viên
    @Query("SELECT dk FROM DangKyHoc dk WHERE UPPER(dk.lopMonHoc.giangVien.hoTen) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<DangKyHoc> findByGiangVienName(@Param("keyword") String keyword);
    
    // 📌 Tìm kiếm kết hợp
    @Query("SELECT dk FROM DangKyHoc dk WHERE " +
           "UPPER(dk.sinhVien.hoTen) LIKE UPPER(CONCAT('%', :keyword, '%')) OR " +
           "UPPER(dk.lopMonHoc.monHoc.tenMon) LIKE UPPER(CONCAT('%', :keyword, '%')) OR " +
           "UPPER(dk.lopMonHoc.giangVien.hoTen) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<DangKyHoc> findByKeyword(@Param("keyword") String keyword);
    
    // 📌 Đếm số lượng đăng ký của một lớp
    long countByLopMonHoc(LopMonHoc lopMonHoc);
    
    // 📌 Tìm các đăng ký có điểm hoặc chưa có điểm
    @Query("SELECT dk FROM DangKyHoc dk WHERE dk.diem IS NOT NULL ORDER BY dk.ngayDangKy DESC")
    List<DangKyHoc> findAllWithScores();
    
    @Query("SELECT dk FROM DangKyHoc dk WHERE dk.diem IS NULL ORDER BY dk.ngayDangKy DESC")
    List<DangKyHoc> findAllWithoutScores();
}
