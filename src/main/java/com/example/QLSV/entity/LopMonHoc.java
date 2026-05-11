package com.example.QLSV.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "lop_mon_hoc")
public class LopMonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLopMonHoc;

    // FK -> MonHoc
    @ManyToOne
    @JoinColumn(name = "ma_mon")
    private MonHoc monHoc;

    // FK -> GiangVien
    @ManyToOne
    @JoinColumn(name = "ma_gv")
    private GiangVien giangVien;

    @JsonIgnore
    @OneToMany(mappedBy = "lopMonHoc", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DangKyHoc> danhSachDangKy;

    private String nhom;

    private Integer siSoToiDa;

    private LocalDate ngayHoc;

    private String phongHoc;

    private Integer tietBatDau;

    private Integer tietKetThuc;

    // ===== getter setter =====

    public Long getMaLopMonHoc() {
        return maLopMonHoc;
    }

    public void setMaLopMonHoc(Long maLopMonHoc) {
        this.maLopMonHoc = maLopMonHoc;
    }

    public MonHoc getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(MonHoc monHoc) {
        this.monHoc = monHoc;
    }

    public GiangVien getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(GiangVien giangVien) {
        this.giangVien = giangVien;
    }

    public String getNhom() {
        return nhom;
    }

    public void setNhom(String nhom) {
        this.nhom = nhom;
    }

    public Integer getSiSoToiDa() {
        return siSoToiDa;
    }

    public void setSiSoToiDa(Integer siSoToiDa) {
        this.siSoToiDa = siSoToiDa;
    }

    public LocalDate getNgayHoc() {
        return ngayHoc;
    }

    public void setNgayHoc(LocalDate ngayHoc) {
        this.ngayHoc = ngayHoc;
    }

    public String getPhongHoc() {
        return phongHoc;
    }

    public void setPhongHoc(String phongHoc) {
        this.phongHoc = phongHoc;
    }

    public Integer getTietBatDau() {
        return tietBatDau;
    }

    public void setTietBatDau(Integer tietBatDau) {
        this.tietBatDau = tietBatDau;
    }

    public Integer getTietKetThuc() {
        return tietKetThuc;
    }

    public void setTietKetThuc(Integer tietKetThuc) {
        this.tietKetThuc = tietKetThuc;
    }

    public List<DangKyHoc> getDanhSachDangKy() {
        return danhSachDangKy;
    }

    public void setDanhSachDangKy(List<DangKyHoc> danhSachDangKy) {
        this.danhSachDangKy = danhSachDangKy;
    }
}