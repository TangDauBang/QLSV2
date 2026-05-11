package com.example.QLSV.service;

import com.example.QLSV.entity.Diem;
import com.example.QLSV.entity.DangKyHoc;
import com.example.QLSV.repository.DiemRepository;
import com.example.QLSV.repository.DangKyHocRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiemService {

    @Autowired
    private DiemRepository diemRepository;

    @Autowired
    private DangKyHocRepository dangKyHocRepository;

    // 📌 Lấy điểm theo ID
    public Diem getDiemById(Long id) {
        return diemRepository.findById(id).orElse(null);
    }

    // 📌 Lấy điểm của một đăng ký
    public Diem getDiemByDangKyHoc(Long maDangKy) {
        Optional<Diem> diem = diemRepository.findByDangKyHocId(maDangKy);
        return diem.orElse(null);
    }

    // 📌 Tạo hoặc cập nhật điểm
    public Diem saveDiem(Long maDangKy, Double diemChuyenCan, Double diemGiuaKy, 
                         Double diemCuoiKy) {
        DangKyHoc dk = dangKyHocRepository.findById(maDangKy).orElse(null);
        if (dk == null) {
            throw new RuntimeException("Đăng ký không tồn tại!");
        }

        Diem diem = dk.getDiem();
        
        // Nếu chưa có điểm, tạo mới
        if (diem == null) {
            diem = new Diem();
            diem.setDangKyHoc(dk);
        }

        // Cập nhật điểm
        diem.setDiemChuyenCan(diemChuyenCan);
        diem.setDiemGiuaKy(diemGiuaKy);
        diem.setDiemCuoiKy(diemCuoiKy);

        // Tính điểm tổng kết: TK = CC×0.1 + GK×0.3 + CK×0.6
        double diemTongKet = calculateTotalScore(diemChuyenCan, diemGiuaKy, diemCuoiKy);
        diem.setDiemTongKet(diemTongKet);

        // Lưu
        Diem savedDiem = diemRepository.save(diem);
        
        // Cập nhật điểm vào DangKyHoc
        dk.setDiem(savedDiem);
        dangKyHocRepository.save(dk);

        return savedDiem;
    }

    // 📌 Tính điểm tổng kết
    public double calculateTotalScore(Double cc, Double gk, Double ck) {
        // Nếu không có điểm nào, trả về 0
        if ((cc == null || cc == 0) && (gk == null || gk == 0) && (ck == null || ck == 0)) {
            return 0;
        }

        double chuyenCan = cc != null ? cc : 0;
        double giuaKy = gk != null ? gk : 0;
        double cuoiKy = ck != null ? ck : 0;

        // Công thức: TK = CC×0.1 + GK×0.3 + CK×0.6
        double totalScore = (chuyenCan * 0.1) + (giuaKy * 0.3) + (cuoiKy * 0.6);
        
        // Làm tròn 2 chữ số thập phân
        return Math.round(totalScore * 100.0) / 100.0;
    }

    // 📌 Xóa điểm
    public void deleteDiem(Long diemId) {
        diemRepository.deleteById(diemId);
    }

    // 📌 Xóa điểm của một đăng ký
    public void deleteScoreByDangKyHoc(Long maDangKy) {
        Diem diem = getDiemByDangKyHoc(maDangKy);
        if (diem != null) {
            deleteDiem(diem.getMaDiem());
        }
    }

    // 📌 Kiểm tra xếp loại
    public String getGrade(Double totalScore) {
        if (totalScore == null || totalScore == 0) {
            return "Chưa đánh giá";
        }

        if (totalScore >= 8.5) {
            return "Xuất sắc (A)";
        } else if (totalScore >= 7.0) {
            return "Tốt (B)";
        } else if (totalScore >= 5.5) {
            return "Khá (C)";
        } else if (totalScore >= 4.0) {
            return "Đạt (D)";
        } else {
            return "Không đạt (F)";
        }
    }

    // 📌 Kiểm tra pass/fail
    public boolean isPassed(Double totalScore) {
        return totalScore != null && totalScore >= 4.0;
    }
}