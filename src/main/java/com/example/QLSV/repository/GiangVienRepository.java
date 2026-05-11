package com.example.QLSV.repository;

import com.example.QLSV.entity.GiangVien;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GiangVienRepository extends JpaRepository<GiangVien, Long> {
    
    @Query("SELECT g FROM GiangVien g WHERE UPPER(g.hoTen) LIKE UPPER(CONCAT('%', :hoTen, '%')) ESCAPE '\\'")   
    List<GiangVien> findByHoTenContainingIgnoreCase(String hoTen);
}
