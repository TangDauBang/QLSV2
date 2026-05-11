package com.example.QLSV.entity;

import com.example.QLSV.enums.GioiTinh;
import com.example.QLSV.enums.TrangThai;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;



@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maSV;

    private String hoTen;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;

    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    private String email;

    private String sdt;

    private String khoa;

    @Enumerated(EnumType.STRING)
    private TrangThai trangThai;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", unique = true)
    @JsonIgnore
    private Account account;


    @OneToMany(mappedBy = "sinhVien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore 
    private List<DangKyHoc> danhSachDangKy;

    // ===== Constructor =====
    public SinhVien() {
    }

    
    // ===== Getter & Setter =====
    public Long getMaSV() {
        return maSV;
    }

    public void setMaSV(Long maSV) {
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public GioiTinh getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(GioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<DangKyHoc> getDanhSachDangKy() {
        return danhSachDangKy;
    }

    public void setDanhSachDangKy(List<DangKyHoc> danhSachDangKy) {
        this.danhSachDangKy = danhSachDangKy;
    }
}