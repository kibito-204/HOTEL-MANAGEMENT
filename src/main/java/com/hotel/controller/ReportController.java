package com.hotel.controller;

import com.hotel.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    // Báo cáo doanh thu theo ngày
    @GetMapping("/revenue-by-day")
    public ResponseEntity<Map<LocalDate, BigDecimal>> getRevenueByDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(reportService.getRevenueByDay(from, to));
    }

    // Báo cáo tình trạng phòng
    @GetMapping("/room-status")
    public ResponseEntity<Map<String, Long>> getRoomStatusReport() {
        return ResponseEntity.ok(reportService.getRoomStatusReport());
    }

    // Báo cáo dịch vụ sử dụng nhiều nhất
    @GetMapping("/top-services")
    public ResponseEntity<List<Map<String, Object>>> getTopUsedServices(
            @RequestParam(defaultValue = "5") int topN,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(reportService.getTopUsedServices(topN, from, to));
    }

    // Báo cáo doanh thu theo tháng trong năm
    @GetMapping("/revenue-by-month")
    public ResponseEntity<Map<Integer, java.math.BigDecimal>> getRevenueByMonth(@RequestParam int year) {
        return ResponseEntity.ok(reportService.getRevenueByMonth(year));
    }

    // Báo cáo doanh thu theo năm
    @GetMapping("/revenue-by-year")
    public ResponseEntity<Map<Integer, java.math.BigDecimal>> getRevenueByYear(@RequestParam int fromYear, @RequestParam int toYear) {
        return ResponseEntity.ok(reportService.getRevenueByYear(fromYear, toYear));
    }
} 