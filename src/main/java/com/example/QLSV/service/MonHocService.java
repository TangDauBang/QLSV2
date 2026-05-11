package com.example.QLSV.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.QLSV.entity.MonHoc;
import com.example.QLSV.repository.MonHocRepository;

@Service
public class MonHocService {

    @Autowired
    private  MonHocRepository monHocRepository;

    

    public List<MonHoc> searchByTenMon(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return monHocRepository.findByTenMonContainingIgnoreCase(keyword);
    }

    public MonHoc createMonHoc(MonHoc monHoc) {
        return monHocRepository.save(monHoc);
    }

    public List<MonHoc> getAll() {
        return monHocRepository.findAll();
    }

    public MonHoc getMonHocById(Long id) {
        return monHocRepository.findById(id)
                .orElse(null);
    }

    public MonHoc updateMonHoc(Long id, MonHoc newData) {
        MonHoc monHoc = getMonHocById(id);

        monHoc.setTenMon(newData.getTenMon());
        monHoc.setSoTinChi(newData.getSoTinChi());
        monHoc.setKhoa(newData.getKhoa());
        return monHocRepository.save(monHoc);
    }

    public void deleteMonHocById(Long id) {
        monHocRepository.deleteById(id);
    }
}