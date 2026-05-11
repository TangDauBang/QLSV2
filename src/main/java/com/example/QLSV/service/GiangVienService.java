package com.example.QLSV.service;

import com.example.QLSV.entity.Account;
import com.example.QLSV.entity.GiangVien;
import com.example.QLSV.enums.Role;
import com.example.QLSV.repository.AccountRepository;
import com.example.QLSV.repository.GiangVienRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiangVienService {

    @Autowired
    private GiangVienRepository gvRepo;
    @Autowired
    private AccountRepository accountRepository;

    public List<GiangVien> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return gvRepo.findByHoTenContainingIgnoreCase(keyword);
    }

    public List<GiangVien> getAll() {
        return gvRepo.findAll();
    }

    public GiangVien getGiangVienById(Long id) {
        return gvRepo.findById(id).orElse(null);
    }

    public GiangVien createGiangVien(GiangVien gv) {
        Account acc = new Account();
        acc.setUsername(gv.getEmail());
        acc.setPassword("1");
        acc.setRole(Role.GIANG_VIEN);
        acc.setGiangVien(gv);
        gv.setAccount(acc);
        accountRepository.save(acc);
        return gvRepo.save(gv);
    }

    public GiangVien updateGiangVien(Long id, GiangVien gv) {
        GiangVien old = gvRepo.findById(id).orElse(null);
        if (old != null) {
            old.setHoTen(gv.getHoTen());
            old.setEmail(gv.getEmail());
            old.setSdt(gv.getSdt());
            old.setKhoa(gv.getKhoa());
            return gvRepo.save(old);
        }
        return null;
    }

    public void deleteGiangVienById(Long id) {
        gvRepo.deleteById(id);
    }
}