package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public interface ReportService {
    Map<LocalDate, BigDecimal> getRevenueByDay(LocalDate start, LocalDate end);
    Map<String, BigDecimal> getRevenueByMonth(int year);
    Map<String, BigDecimal> getRevenueByYear(int year1, int year2);
    Map<String, Long> getRoomStatus();
    Map<String, Long> getTopService();
}
