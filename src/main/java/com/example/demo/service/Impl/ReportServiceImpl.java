package com.example.demo.service.Impl;

import com.example.demo.entity.RoomStatus;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UsedServiceRepository;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UsedServiceRepository usedServiceRepository;
    public BigDecimal getRevenue(LocalDate start, LocalDate end){
        return reservationRepository.findByCheckoutBetween(start.atStartOfDay(), end.atTime(23,59,59)).stream()
                .map(reservation -> reservation.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Override
    public Map<LocalDate,BigDecimal> getRevenueByDay(LocalDate start, LocalDate end){
        Map<LocalDate,BigDecimal> x = new HashMap<>();
        while(!start.isAfter(end)){
            BigDecimal total = getRevenue(start,start);
            x.put(start,total);
            start = start.plusDays(1);
        }
        return x;
    }
    @Override
    public Map<String, BigDecimal> getRevenueByMonth(int year){
        Map<String, BigDecimal> x = new LinkedHashMap<>(); 
        for(int i = 1;i<= 12;i++){
            LocalDate startofMonth = LocalDate.of(year, i, 1);
            LocalDate endofMonth = startofMonth.withDayOfMonth(startofMonth.lengthOfMonth());
            x.put("Tháng " + i,getRevenue(startofMonth,endofMonth));
        }
        return x;
    }
    @Override
    public Map<String, BigDecimal> getRevenueByYear(int start, int end){
        Map<String, BigDecimal> x = new HashMap<>();
        for(int i = start;i<=end;i++){
            LocalDate s = LocalDate.of(i,1,1);
            LocalDate e = LocalDate.of(i,12,31);
            x.put("Năm "+i,getRevenue(s,e));
        }
        return x;
    }
    @Override
    public Map<String, Long> getRoomStatus(){
        Map<String,Long> x = new HashMap<>();
        List<Object[]> abc = roomRepository.listRoomStatus();
        for(Object[] a:abc){
            x.put(a[0].toString(),(Long)a[1]);
        }
        return x;
    }
    @Override
    public Map<String, Long> getTopService(){
        Map<String, Long> a = new LinkedHashMap<>();
        List<Object[]> x = usedServiceRepository.listTopService();
        for(Object[] y : x){
            a.put(y[0].toString(),(Long)y[1]);
        }
        return a;
    }
}
