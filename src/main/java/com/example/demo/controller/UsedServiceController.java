package com.example.demo.controller;

import com.example.demo.dto.UsedServiceDTO;
import com.example.demo.service.UsedServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usedservice")
public class UsedServiceController {
    @Autowired
    private UsedServiceService usedServiceService;
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<UsedServiceDTO> addService(@RequestBody UsedServiceDTO dto){
        UsedServiceDTO dto1 = usedServiceService.addService(dto);
        return  ResponseEntity.ok(dto1);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'MANAGER')")
    public ResponseEntity<String> sumTotal(@PathVariable Long id){
        String abc = usedServiceService.sumTotal(id);
        return ResponseEntity.ok(abc);
    }
}
