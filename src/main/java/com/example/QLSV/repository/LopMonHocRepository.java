package com.example.QLSV.repository;

import com.example.QLSV.entity.LopMonHoc;
import com.example.QLSV.entity.MonHoc;
import com.example.QLSV.entity.GiangVien;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LopMonHocRepository extends JpaRepository<LopMonHoc, Long> {
    
    // 📌 Tìm kiếm theo môn học
    @Query("SELECT l FROM LopMonHoc l WHERE UPPER(l.monHoc.tenMon) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<LopMonHoc> findByMonHocNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    // 📌 Tìm kiếm theo giảng viên
    @Query("SELECT l FROM LopMonHoc l WHERE UPPER(l.giangVien.hoTen) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<LopMonHoc> findByGiangVienNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    // 📌 Tìm kiếm theo khoa
    @Query("SELECT l FROM LopMonHoc l WHERE UPPER(l.monHoc.khoa) LIKE UPPER(CONCAT('%', :khoa, '%'))")
    List<LopMonHoc> findByKhoaContainingIgnoreCase(@Param("khoa") String khoa);
    
    // 📌 Tìm lớp theo ngày học (thứ trong tuần)
    List<LopMonHoc> findByNgayHoc(String ngayHoc);
    
    // 📌 Tìm tất cả lớp của một giảng viên
    List<LopMonHoc> findByGiangVien(GiangVien giangVien);
    
    // 📌 Tìm tất cả lớp của một môn học
    List<LopMonHoc> findByMonHoc(MonHoc monHoc);
    
    // 📌 Kiểm tra giảng viên có lớp cùng ngày, cùng tiết không (để kiểm tra trùng lịch)
    @Query("SELECT l FROM LopMonHoc l WHERE l.giangVien.maGV = :maGV " +
           "AND l.ngayHoc = :ngayHoc " +
           "AND ((l.tietBatDau <= :tietKetThuc AND l.tietKetThuc >= :tietBatDau))")
    List<LopMonHoc> findConflictingClassesByTeacher(
        @Param("maGV") Long maGV,
        @Param("ngayHoc") String ngayHoc,
        @Param("tietBatDau") Integer tietBatDau,
        @Param("tietKetThuc") Integer tietKetThuc
    );
    
    // 📌 Kiểm tra phòng học có bị trùng không
    @Query("SELECT l FROM LopMonHoc l WHERE l.phongHoc = :phongHoc " +
           "AND l.ngayHoc = :ngayHoc " +
           "AND ((l.tietBatDau <= :tietKetThuc AND l.tietKetThuc >= :tietBatDau))")
    List<LopMonHoc> findConflictingClassesByRoom(
        @Param("phongHoc") String phongHoc,
        @Param("ngayHoc") String ngayHoc,
        @Param("tietBatDau") Integer tietBatDau,
        @Param("tietKetThuc") Integer tietKetThuc
    );
    
    // 📌 Kiểm tra trùng lịch (khi chỉnh sửa, bỏ qua chính nó)
    @Query("SELECT l FROM LopMonHoc l WHERE l.giangVien.maGV = :maGV " +
           "AND l.ngayHoc = :ngayHoc " +
           "AND ((l.tietBatDau <= :tietKetThuc AND l.tietKetThuc >= :tietBatDau)) " +
           "AND l.maLopMonHoc != :maLopMonHoc")
    List<LopMonHoc> findConflictingTeacherClassesExcludingThis(
        @Param("maGV") Long maGV,
        @Param("ngayHoc") String ngayHoc,
        @Param("tietBatDau") Integer tietBatDau,
        @Param("tietKetThuc") Integer tietKetThuc,
        @Param("maLopMonHoc") Long maLopMonHoc
    );
    
    // 📌 Kiểm tra phòng trùng (khi chỉnh sửa)
    @Query("SELECT l FROM LopMonHoc l WHERE l.phongHoc = :phongHoc " +
           "AND l.ngayHoc = :ngayHoc " +
           "AND ((l.tietBatDau <= :tietKetThuc AND l.tietKetThuc >= :tietBatDau)) " +
           "AND l.maLopMonHoc != :maLopMonHoc")
    List<LopMonHoc> findConflictingRoomClassesExcludingThis(
        @Param("phongHoc") String phongHoc,
        @Param("ngayHoc") String ngayHoc,
        @Param("tietBatDau") Integer tietBatDau,
        @Param("tietKetThuc") Integer tietKetThuc,
        @Param("maLopMonHoc") Long maLopMonHoc
    );
}