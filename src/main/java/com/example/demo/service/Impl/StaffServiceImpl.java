package com.example.demo.service.Impl;

import com.example.demo.dto.StaffDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.StaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<StaffDTO> getStaff(){
        List<StaffDTO>dtos = userRepository.findAll().stream()
                .map(u -> {
                    StaffDTO dto = new StaffDTO();
                    BeanUtils.copyProperties(u, dto);
                    dto.setSalary(BigDecimal.ZERO);
                    return dto;
                })
                .collect(Collectors.toList());
        return dtos;
    }
//    @Override
//    public StaffDTO createStaff(StaffDTO dto){
//        Staff staff = new Staff();
//        BeanUtils.copyProperties(dto,staff);
//        staff = staffRepository.save(staff);
//        BeanUtils.copyProperties(staff,dto);
//        return dto;
//    }
    @Override
    public StaffDTO updateStaff(StaffDTO dto, Long id){
        User u = userRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
        BeanUtils.copyProperties(dto, u,"username", "id", "email");
        u = userRepository.save(u);
        BeanUtils.copyProperties(u, dto);
        return dto;
    }
    @Override
    public void deleteStaff(Long id){
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"));
        userRepository.delete(u);
    }
}
