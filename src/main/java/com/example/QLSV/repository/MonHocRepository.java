package com.example.QLSV.repository;

import com.example.QLSV.entity.MonHoc;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MonHocRepository extends JpaRepository<MonHoc, Long> {
    
    @Query("SELECT m FROM MonHoc m WHERE UPPER(m.tenMon) LIKE UPPER(CONCAT('%', :tenMon, '%')) ESCAPE '\\'")
    List<MonHoc> findByTenMonContainingIgnoreCase(String tenMon);
}
