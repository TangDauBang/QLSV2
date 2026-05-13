package com.example.QLSV.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "giang_vien")
public class GiangVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maGV;

    private String hoTen;
    private String email;
    private String sdt;
    private String khoa;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", unique = true)
    @JsonIgnore
    private Account account;


    @OneToMany(mappedBy = "giangVien", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LopMonHoc> lopMonHocs;


    // Getter Setter
    public Long getMaGV() {
        return maGV;
    }

    public void setMaGV(Long maGV) {
        this.maGV = maGV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<LopMonHoc> getLopMonHocs() {
        return lopMonHocs;
    }


    public void setLopMonHocs(List<LopMonHoc> lopMonHocs) {
        this.lopMonHocs = lopMonHocs;
    }
}
