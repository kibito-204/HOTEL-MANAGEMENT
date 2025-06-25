package com.hotel.service.impl;

import com.hotel.entity.RoomStatus;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.ServiceUsageRepository;
import com.hotel.repository.HotelServiceRepository;
import com.hotel.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ServiceUsageRepository serviceUsageRepository;
    @Autowired
    private HotelServiceRepository hotelServiceRepository;

    @Override
    public BigDecimal getRevenueReport(LocalDate from, LocalDate to) {
        // Tổng doanh thu từ đặt phòng đã CHECKED_OUT trong khoảng thời gian
        return reservationRepository.findByCheckOutBetween(
                from.atStartOfDay(), to.atTime(LocalTime.MAX))
                .stream()
                .filter(r -> r.getStatus() == com.hotel.entity.ReservationStatus.CHECKED_OUT)
                .map(r -> {
                    BigDecimal total = r.getTotalAmount();
                    BigDecimal serviceTotal = serviceUsageRepository.sumTotalPriceByReservationId(r.getId());
                    return total.add(serviceTotal != null ? serviceTotal : BigDecimal.ZERO);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<LocalDate, BigDecimal> getRevenueByDay(LocalDate from, LocalDate to) {
        Map<LocalDate, BigDecimal> result = new HashMap<>();
        LocalDate current = from;
        while (!current.isAfter(to)) {
            BigDecimal revenue = getRevenueReport(current, current);
            result.put(current, revenue);
            current = current.plusDays(1);
        }
        return result;
    }

    @Override
    public Map<String, Long> getRoomStatusReport() {
        Map<String, Long> result = new HashMap<>();
        for (RoomStatus status : RoomStatus.values()) {
            long count = roomRepository.findByStatus(status).size();
            result.put(status.name(), count);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getTopUsedServices(int topN, LocalDate from, LocalDate to) {
        List<Object[]> raw = serviceUsageRepository.findTopUsedServices(
                from.atStartOfDay(), to.atTime(LocalTime.MAX), topN);
        return raw.stream().map(arr -> {
            Map<String, Object> map = new HashMap<>();
            map.put("serviceName", arr[0]);
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, BigDecimal> getRevenueByMonth(int year) {
        Map<Integer, BigDecimal> result = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            LocalDate from = LocalDate.of(year, month, 1);
            LocalDate to = from.withDayOfMonth(from.lengthOfMonth());
            BigDecimal revenue = getRevenueReport(from, to);
            result.put(month, revenue);
        }
        return result;
    }

    @Override
    public Map<Integer, BigDecimal> getRevenueByYear(int fromYear, int toYear) {
        Map<Integer, BigDecimal> result = new HashMap<>();
        for (int year = fromYear; year <= toYear; year++) {
            LocalDate from = LocalDate.of(year, 1, 1);
            LocalDate to = LocalDate.of(year, 12, 31);
            BigDecimal revenue = getRevenueReport(from, to);
            result.put(year, revenue);
        }
        return result;
    }
} 