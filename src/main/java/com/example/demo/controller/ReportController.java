package com.example.demo.controller;


import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Map<LocalDate, BigDecimal>>  getRevenueByDay(LocalDate start, LocalDate end){
        return ResponseEntity.ok(reportService.getRevenueByDay(start, end));
    }

}
