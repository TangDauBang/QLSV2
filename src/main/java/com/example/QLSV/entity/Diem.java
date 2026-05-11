package com.example.QLSV.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "diem")
public class Diem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDiem;

    // ===== Quan hệ 1-1 với DangKyHoc =====
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_dang_ky", nullable = false, unique = true)
    private DangKyHoc dangKyHoc;

    // ===== Các loại điểm =====
    private Double diemChuyenCan;
    private Double diemGiuaKy;
    private Double diemCuoiKy;
    private Double diemTongKet;

    // ===== Getter Setter =====

    public Long getMaDiem() {
        return maDiem;
    }

    public void setMaDiem(Long maDiem) {
        this.maDiem = maDiem;
    }

    public DangKyHoc getDangKyHoc() {
        return dangKyHoc;
    }

    public void setDangKyHoc(DangKyHoc dangKyHoc) {
        this.dangKyHoc = dangKyHoc;
    }

    public Double getDiemChuyenCan() {
        return diemChuyenCan;
    }

    public void setDiemChuyenCan(Double diemChuyenCan) {
        this.diemChuyenCan = diemChuyenCan;
    }

    public Double getDiemGiuaKy() {
        return diemGiuaKy;
    }

    public void setDiemGiuaKy(Double diemGiuaKy) {
        this.diemGiuaKy = diemGiuaKy;
    }

    public Double getDiemCuoiKy() {
        return diemCuoiKy;
    }

    public void setDiemCuoiKy(Double diemCuoiKy) {
        this.diemCuoiKy = diemCuoiKy;
    }

    public Double getDiemTongKet() {
        return diemTongKet;
    }

    public void setDiemTongKet(Double diemTongKet) {
        this.diemTongKet = diemTongKet;
    }
}