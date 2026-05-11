package com.example.QLSV.repository;

import com.example.QLSV.entity.SinhVien;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {

    @Query("SELECT s FROM SinhVien s WHERE UPPER(s.hoTen) LIKE UPPER(CONCAT('%', :hoTen, '%')) ESCAPE '\\'")
    List<SinhVien> findByHoTenContainingIgnoreCase(String hoTen);
}
