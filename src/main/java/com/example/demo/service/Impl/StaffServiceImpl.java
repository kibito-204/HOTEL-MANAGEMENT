package com.example.demo.service.Impl;

import com.example.demo.dto.StaffDTO;
import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;
import com.example.demo.service.StaffService;
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
    public List<StaffDTO> getStaff(){
        return staffRepository.findAll().stream()
                .map(staff -> {
                    StaffDTO dto = new StaffDTO();
                    BeanUtils.copyProperties(staff, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public StaffDTO createStaff(StaffDTO dto){
        Staff staff = new Staff();
        BeanUtils.copyProperties(dto, staff);
        staff = staffRepository.save(staff);
        BeanUtils.copyProperties(staff, dto);
        return dto;
    }
    @Override
    public StaffDTO updateStaff(StaffDTO dto, Long id){
        Staff staff = staffRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        BeanUtils.copyProperties(dto, staff, "id");
        staff = staffRepository.save(staff);
        BeanUtils.copyProperties(staff, dto);
        return dto;
    }
    @Override
    public void deleteStaff(Long id){
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        staffRepository.delete(staff);
    }
}
