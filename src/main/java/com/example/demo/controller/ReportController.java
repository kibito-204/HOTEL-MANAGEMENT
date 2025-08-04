package com.example.demo.controller;


import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/revenue-by-day")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<LocalDate, BigDecimal>> getRevenueByDay(@RequestParam LocalDate start,@RequestParam LocalDate end){
        return ResponseEntity.ok(reportService.getRevenueByDay(start, end));
    }
    @GetMapping("/revenue-by-month")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueByMonth( @RequestParam int year){
        return ResponseEntity.ok(reportService.getRevenueByMonth(year));
    }
    @GetMapping("/revenue-by-year")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, BigDecimal>> getRevenueByYear(@RequestParam int year1, @RequestParam int year2){
        return ResponseEntity.ok(reportService.getRevenueByYear(year1, year2));
    }
    @GetMapping("/room-status")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Long>> getRoomStatus(){
        return  ResponseEntity.ok(reportService.getRoomStatus());
    }
    @GetMapping("/top-service")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Map<String, Long>> getTopService(){
        return ResponseEntity.ok(reportService.getTopService());
    }
}
