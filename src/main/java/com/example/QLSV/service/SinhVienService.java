package com.example.QLSV.service;

import com.example.QLSV.entity.Account;
import com.example.QLSV.entity.SinhVien;
import com.example.QLSV.enums.Role;
import com.example.QLSV.enums.TrangThai;
import com.example.QLSV.repository.AccountRepository;
import com.example.QLSV.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SinhVienService {

    @Autowired
    private SinhVienRepository svRepo;

    @Autowired
    private AccountRepository accountRepository;

    public List<SinhVien> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return svRepo.findByHoTenContainingIgnoreCase(keyword);
    }
    // Lấy tất cả
    public List<SinhVien> getAll() {
        return svRepo.findAll();
    }

    // Lấy theo ID
    public SinhVien getSinhVienById(Long id) {
        Optional<SinhVien> sv = svRepo.findById(id);
        return sv.orElse(null);
    }

    // Thêm mới
    public SinhVien createSinhVien(SinhVien sv) {
        Account acc = new Account();
        acc.setUsername(sv.getEmail());
        acc.setPassword("1");
        acc.setRole(Role.SINH_VIEN);
        acc.setSinhVien(sv);
        accountRepository.save(acc);
        sv.setAccount(acc);
        sv.setTrangThai(TrangThai.DANG_HOC);
        return svRepo.save(sv);
    }

    // Cập nhật
    public SinhVien updateSinhVien(Long id, SinhVien sv) {
        Optional<SinhVien> existing = svRepo.findById(id);

        if (existing.isPresent()) {
            SinhVien old = existing.get();

            old.setHoTen(sv.getHoTen());
            old.setNgaySinh(sv.getNgaySinh());
            old.setGioiTinh(sv.getGioiTinh());
            old.setEmail(sv.getEmail());
            old.setSdt(sv.getSdt());
            old.setKhoa(sv.getKhoa());
            old.setTrangThai(sv.getTrangThai());

            return svRepo.save(old);
        }

        return null; // hoặc throw exception
    }

    // Xóa
    public boolean deleteSinhVienById(Long id) {
        if (svRepo.existsById(id)) {
            svRepo.deleteById(id);
            return true;
        }
        return false;
    }
}