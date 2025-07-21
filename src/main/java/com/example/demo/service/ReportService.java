package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface ReportService {
    Map<LocalDate, BigDecimal> getRevenueByDay(LocalDate start, LocalDate end);
}
