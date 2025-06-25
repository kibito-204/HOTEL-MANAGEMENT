package com.hotel.service;

import com.hotel.dto.StaffDTO;
import java.util.List;

public interface StaffService {
    StaffDTO createStaff(StaffDTO staffDTO);
    StaffDTO updateStaff(Long id, StaffDTO staffDTO);
    void deleteStaff(Long id);
    StaffDTO getStaffById(Long id);
    List<StaffDTO> getAllStaff();
} 