package com.hotel.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {
    // Báo cáo doanh thu theo khoảng thời gian
    BigDecimal getRevenueReport(LocalDate from, LocalDate to);

    // Báo cáo doanh thu theo từng ngày trong khoảng
    Map<LocalDate, BigDecimal> getRevenueByDay(LocalDate from, LocalDate to);

    // Báo cáo doanh thu theo tháng trong năm
    Map<Integer, BigDecimal> getRevenueByMonth(int year);

    // Báo cáo doanh thu theo năm
    Map<Integer, BigDecimal> getRevenueByYear(int fromYear, int toYear);

    // Báo cáo tình trạng phòng
    Map<String, Long> getRoomStatusReport();

    // Báo cáo dịch vụ được sử dụng nhiều nhất
    List<Map<String, Object>> getTopUsedServices(int topN, LocalDate from, LocalDate to);
} 