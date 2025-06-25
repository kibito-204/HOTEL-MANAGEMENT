package com.hotel.service.impl;

import com.hotel.dto.ReservationDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.Reservation;
import com.hotel.entity.Room;
import com.hotel.entity.RoomStatus;
import com.hotel.entity.ReservationStatus;
import com.hotel.entity.RoomType;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.exception.ValidationException;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.service.ReservationService;
import com.hotel.service.ServiceUsageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ServiceUsageService serviceUsageService;

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        // Kiểm tra khách hàng tồn tại
        Customer customer = customerRepository.findById(reservationDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"));

        // Kiểm tra phòng tồn tại
        Room room = roomRepository.findById(reservationDTO.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng"));

        // Kiểm tra phòng phải đang AVAILABLE mới cho phép đặt
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new ValidationException("Chỉ có thể đặt phòng khi phòng đang ở trạng thái AVAILABLE");
        }

        // Kiểm tra thời gian check-in và check-out
        if (reservationDTO.getCheckIn().isAfter(reservationDTO.getCheckOut())) {
            throw new ValidationException("Thời gian check-in phải trước check-out");
        }

        // Kiểm tra phòng đã được đặt trong khoảng thời gian này chưa (với mọi trạng thái đang/bắt đầu đặt)
        List<Reservation> overlappingReservations = reservationRepository.findByRoomId(room.getId());
        for (Reservation existingReservation : overlappingReservations) {
            // Bỏ qua nếu là update và là chính booking này
            if (reservationDTO instanceof ReservationDTO && existingReservation.getId() != null && existingReservation.getId().equals(reservationDTO.getId())) {
                continue;
            }
            if (isOverlapping(reservationDTO.getCheckIn(), reservationDTO.getCheckOut(),
                    existingReservation.getCheckIn(), existingReservation.getCheckOut())
                && (existingReservation.getStatus() == ReservationStatus.PENDING)) {
                throw new ValidationException("Phòng đã được đặt trong khoảng thời gian này");
            }
        }

        Reservation reservation = new Reservation();
        // Chỉ copy các trường cần thiết
        reservation.setCheckIn(reservationDTO.getCheckIn());
        reservation.setCheckOut(reservationDTO.getCheckOut());
        reservation.setTotalAmount(reservationDTO.getTotalAmount());
        reservation.setNote(reservationDTO.getNote());
        reservation.setNumberOfGuests(reservationDTO.getNumberOfGuests());
        
        // Set các trường bắt buộc
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(LocalDateTime.now());

        reservation = reservationRepository.save(reservation);
        
        // Convert back to DTO with additional information
        ReservationDTO result = new ReservationDTO();
        BeanUtils.copyProperties(reservation, result);
        result.setCustomerId(customer.getId());
        result.setCustomerName(customer.getName());
        result.setRoomNumber(room.getRoomNumber());
        result.setRoomType(room.getRoomType());
        result.setRoomId(room.getId());
        return result;
    }

    @Override
    public ReservationDTO updateReservation(Long id, ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đặt phòng"));

        // Không cho phép thao tác nếu đã CHECKED_OUT
        if (existingReservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new ValidationException("Không thể thao tác với phòng đã CHECKED_OUT");
        }

        // Kiểm tra phòng tồn tại
        Room room = roomRepository.findById(reservationDTO.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng"));

        // Kiểm tra phòng phải đang AVAILABLE mới cho phép đặt
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new ValidationException("Chỉ có thể đặt phòng khi phòng đang ở trạng thái AVAILABLE");
        }

        // Kiểm tra thời gian check-in và check-out
        if (reservationDTO.getCheckIn().isAfter(reservationDTO.getCheckOut())) {
            throw new ValidationException("Thời gian check-in phải trước check-out");
        }

        // Kiểm tra phòng đã được đặt trong khoảng thời gian này chưa (với mọi trạng thái đang/bắt đầu đặt)
        List<Reservation> overlappingReservations = reservationRepository.findByRoomId(room.getId());
        for (Reservation existing : overlappingReservations) {
            // Bỏ qua chính reservation đang update
            if (existing.getId() != null && existing.getId().equals(id)) {
                continue;
            }
            if (isOverlapping(reservationDTO.getCheckIn(), reservationDTO.getCheckOut(),
                    existing.getCheckIn(), existing.getCheckOut())
                && (existing.getStatus() == ReservationStatus.CONFIRMED
                    || existing.getStatus() == ReservationStatus.PENDING
                    || existing.getStatus() == ReservationStatus.CHECKED_IN)) {
                throw new ValidationException("Phòng đã được đặt trong khoảng thời gian này");
            }
        }

        BeanUtils.copyProperties(reservationDTO, existingReservation, "id", "customer", "status", "createdAt");
        existingReservation.setRoom(room);
        existingReservation.setUpdatedAt(LocalDateTime.now());
        // Chỉ cho phép cập nhật status sang CANCELLED hoặc CONFIRMED
        if (reservationDTO.getStatus() == ReservationStatus.CANCELLED || reservationDTO.getStatus() == ReservationStatus.CONFIRMED) {
            existingReservation.setStatus(reservationDTO.getStatus());
        }
        existingReservation = reservationRepository.save(existingReservation);
        
        // Convert back to DTO with additional information
        ReservationDTO result = new ReservationDTO();
        BeanUtils.copyProperties(existingReservation, result);
        result.setCustomerId(existingReservation.getCustomer().getId());
        result.setRoomId(existingReservation.getRoom().getId());
        result.setCustomerName(existingReservation.getCustomer().getName());
        result.setRoomNumber(existingReservation.getRoom().getRoomNumber());
        result.setRoomType(existingReservation.getRoom().getRoomType());
        result.setUpdatedAt(LocalDateTime.now());
        
        return result;
    }

    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đặt phòng"));

        // Không cho phép thao tác nếu đã CHECKED_OUT
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new ValidationException("Không thể thao tác với đặt phòng đã CHECKED_OUT");
        }

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new ValidationException("Không thể xóa đặt phòng đã xác nhận");
        }

        reservationRepository.delete(reservation);
    }

    


    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<ReservationDTO> getReservation(Long id, Long customerId, Long roomId){
        return reservationRepository.findAll().stream()
        .filter(reservation -> id == null || id.equals(reservation.getId()))
        .filter(reservation -> roomId == null || roomId.equals(reservation.getRoom().getId()))
        .filter(reservation -> customerId == null || customerId.equals(reservation.getCustomer().getId()))
        .map(this::convertToDTO)
        .collect(Collectors.toList());
    }
    @Override   
    public List<Room> findAvailableRooms(RoomType roomType, LocalDateTime start, LocalDateTime end) {
        List<Room> AvailableRooms = roomRepository.findByRoomTypeAndStatus(roomType ,RoomStatus.AVAILABLE);
        List<Long> BookedRoomIDs = reservationRepository.findBookedRoomIds(start,end);
        if(BookedRoomIDs.isEmpty()){
            return AvailableRooms;
        }
        else{
            return AvailableRooms.stream()
            .filter(room -> !BookedRoomIDs.contains(room.getId()))
            .collect(Collectors.toList());
        }
    }


    private boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        BeanUtils.copyProperties(reservation, dto);
        dto.setCustomerId(reservation.getCustomer().getId());
        dto.setRoomId(reservation.getRoom().getId());
        dto.setCustomerName(reservation.getCustomer().getName());
        dto.setRoomNumber(reservation.getRoom().getRoomNumber());
        dto.setRoomType(reservation.getRoom().getRoomType());
        // Lấy danh sách tên dịch vụ thực tế từ ServiceUsage (HotelService.name)
        java.util.List<String> usageNames = reservation.getServiceUsages() == null ? java.util.Collections.emptyList() :
            reservation.getServiceUsages().stream()
                .filter(su -> su.getService() != null && su.getService().getName() != null)
                .map(su -> su.getService().getName())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        dto.setServices(usageNames);
        // Cộng tổng giá dịch vụ vào totalAmount
        java.math.BigDecimal serviceTotal = serviceUsageService.calculateTotalServiceCost(reservation.getId());
        if (serviceTotal != null) {
            dto.setTotalAmount(reservation.getTotalAmount().add(serviceTotal));
        } else {
            dto.setTotalAmount(reservation.getTotalAmount());
        }
        return dto;
    }

    @Override
    public void checkIn(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đặt phòng"));
        // Không cho phép thao tác nếu đã CHECKED_OUT
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new ValidationException("Không thể thao tác với đặt phòng đã CHECKED_OUT");
        }
        Room room = reservation.getRoom();
        // Chỉ cho phép check-in nếu trạng thái là CONFIRMED
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new ValidationException("Chỉ có thể check-in khi đặt phòng ở trạng thái CONFIRMED!");
        }
        // Cập nhật trạng thái phòng và đặt phòng
        room.setStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservationRepository.save(reservation);
    }

    @Override
    public String checkOut(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đặt phòng"));
        // Không cho phép thao tác nếu đã CHECKED_OUT
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new ValidationException("Không thể thao tác với đặt phòng đã CHECKED_OUT");
        }
        Room room = reservation.getRoom();
        // Chỉ cho phép check-out nếu trạng thái là CHECKED_IN
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new ValidationException("Chỉ có thể check-out khi đặt phòng ở trạng thái CHECKED_IN!");
        }
        // Cập nhật trạng thái phòng và đặt phòng
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);
        LocalDateTime now = LocalDateTime.now();
// Kiểm tra check-out phải sau check-in
        if (!now.isAfter(reservation.getCheckIn())) {
            throw new ValidationException("Thời gian check-out phải sau thời gian check-in!");
        }
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation.setCheckOut(now);
        // Tính lại totalAmount theo số đêm
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
            reservation.getCheckIn().toLocalDate(),
            reservation.getCheckOut().toLocalDate()
        );
        if (nights <= 0) nights = 1; // đảm bảo tối thiểu 1 đêm
        java.math.BigDecimal pricePerNight = reservation.getRoom().getPrice();
        java.math.BigDecimal totalAmount = pricePerNight.multiply(java.math.BigDecimal.valueOf(nights));
        reservation.setTotalAmount(totalAmount);
        reservationRepository.save(reservation);
        // Tính tổng chi phí: tổng tiền phòng + tổng dịch vụ
        java.math.BigDecimal serviceTotal = serviceUsageService.calculateTotalServiceCost(reservationId);
        java.math.BigDecimal total = reservation.getTotalAmount();
        if (serviceTotal != null) {
            total = total.add(serviceTotal);
        }
        return "Tổng chi phí: " + total + " VND";
    }
} 