package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.StaffDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.StaffService;

import java.util.*;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @GetMapping("")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<StaffDTO>> getStaff(){
        List<StaffDTO> dto = staffService.getStaff();
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<StaffDTO> updateStaff(@RequestBody StaffDTO dto, @PathVariable Long id){
        StaffDTO dto1 = staffService.updateStaff(dto, id);
        return ResponseEntity.ok(dto1);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id){
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
