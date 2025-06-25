package com.hotel.controller;

import com.hotel.dto.ServiceUsageDTO;
import com.hotel.service.ServiceUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
@RestController
@RequestMapping("/service-usage")
public class ServiceUsageController {
    @Autowired
    private ServiceUsageService serviceUsageService;

    // Gán dịch vụ cho đặt phòng
    @PostMapping
    public ResponseEntity<ServiceUsageDTO> addServiceUsage(@RequestBody ServiceUsageDTO dto) {
        ServiceUsageDTO result = serviceUsageService.addServiceUsage(dto);
        return ResponseEntity.ok(result);
    }


    // Tính tổng chi phí dịch vụ cho đặt phòng
    @GetMapping("/reservation/{reservationId}/total")
    public ResponseEntity<BigDecimal> getTotalServiceCost(@PathVariable Long reservationId) {
        BigDecimal total = serviceUsageService.calculateTotalServiceCost(reservationId);
        return ResponseEntity.ok(total);
    }
} 