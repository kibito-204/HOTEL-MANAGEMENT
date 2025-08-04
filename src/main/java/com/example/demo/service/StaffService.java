package com.example.demo.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.StaffDTO;

import java.util.List;

public interface StaffService {
    List<StaffDTO> getStaff();
//    StaffDTO createStaff(StaffDTO dto);
    StaffDTO updateStaff(StaffDTO dto, Long id);
    void deleteStaff(Long id);
}
