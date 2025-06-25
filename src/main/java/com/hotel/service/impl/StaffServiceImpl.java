package com.hotel.service.impl;

import com.hotel.dto.StaffDTO;
import com.hotel.entity.Staff;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.StaffRepository;
import com.hotel.service.StaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        Staff staff = new Staff();
        BeanUtils.copyProperties(staffDTO, staff);
        staff = staffRepository.save(staff);
        BeanUtils.copyProperties(staff, staffDTO);
        return staffDTO;
    }

    @Override
    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
        BeanUtils.copyProperties(staffDTO, staff, "id");
        staff = staffRepository.save(staff);
        BeanUtils.copyProperties(staff, staffDTO);
        return staffDTO;
    }

    @Override
    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    @Override
    public StaffDTO getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
        StaffDTO staffDTO = new StaffDTO();
        BeanUtils.copyProperties(staff, staffDTO);
        return staffDTO;
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream().map(staff -> {
            StaffDTO dto = new StaffDTO();
            BeanUtils.copyProperties(staff, dto);
            return dto;
        }).collect(Collectors.toList());
    }
} 