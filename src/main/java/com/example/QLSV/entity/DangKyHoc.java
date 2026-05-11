package com.example.QLSV.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "dang_ky_hoc")
public class DangKyHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDangKy;

    // ===== SinhVien =====
    @ManyToOne
    @JoinColumn(name = "ma_sv", nullable = false)
    private SinhVien sinhVien;

    // ===== LopMonHoc =====
    @ManyToOne
    @JoinColumn(name = "ma_lop_mon_hoc", nullable = false)
    private LopMonHoc lopMonHoc;

    @OneToOne(mappedBy = "dangKyHoc", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Diem diem;

    private LocalDateTime ngayDangKy;

    // ===== Getter Setter =====

    public Long getMaDangKy() {
        return maDangKy;
    }

    public void setMaDangKy(Long maDangKy) {
        this.maDangKy = maDangKy;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public LopMonHoc getLopMonHoc() {
        return lopMonHoc;
    }

    public void setLopMonHoc(LopMonHoc lopMonHoc) {
        this.lopMonHoc = lopMonHoc;
    }

    public LocalDateTime getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDateTime ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public Diem getDiem() {
        return diem;
    }
    public void setDiem(Diem diem) {
        this.diem = diem;
    }
}