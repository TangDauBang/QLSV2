package com.example.QLSV.repository;

import com.example.QLSV.entity.Diem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiemRepository extends JpaRepository<Diem, Long> {
    
    // 📌 Tìm điểm theo mã đăng ký
    @Query("SELECT d FROM Diem d WHERE d.dangKyHoc.maDangKy = :maDangKy")
    Optional<Diem> findByDangKyHocId(@Param("maDangKy") Long maDangKy);
}