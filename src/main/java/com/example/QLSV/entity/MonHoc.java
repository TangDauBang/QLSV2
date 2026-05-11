package com.example.QLSV.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "mon_hoc")
public class MonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maMon;

    private String tenMon;

    private Integer soTinChi;

    private String khoa;

    @OneToMany(mappedBy = "monHoc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LopMonHoc> lopMonHocs;

    // getter setter

    public Long getMaMon() {
        return maMon;
    }

    public void setMaMon(Long maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public Integer getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Integer soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public List<LopMonHoc> getLopMonHocs() {
        return lopMonHocs;
    }

    public void setLopMonHocs(List<LopMonHoc> lopMonHocs) {
        this.lopMonHocs = lopMonHocs;
    }
}