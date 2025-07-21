package com.example.demo.service.Impl;

import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Override
    public Map<LocalDate,BigDecimal> getRevenueByDay(LocalDate start, LocalDate end){
        
    }
}
