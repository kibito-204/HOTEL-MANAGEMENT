package com.example.demo.controller;

import com.example.demo.dto.StaffDTO;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @GetMapping("")
    public ResponseEntity<List<StaffDTO>> getStaff(){
        List<StaffDTO> dto = staffService.getStaff();
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/create")
    public ResponseEntity<StaffDTO> createStaff(@RequestBody StaffDTO dto){
        StaffDTO dto1 = staffService.createStaff(dto);
        return ResponseEntity.ok(dto1);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<StaffDTO> updateStaff(@RequestBody StaffDTO dto, @PathVariable Long id){
        StaffDTO dto1 = staffService.updateStaff(dto, id);
        return ResponseEntity.ok(dto1);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id){
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
