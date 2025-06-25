package com.hotel.service;

import com.hotel.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    // Thêm khách hàng mới
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    
    // Cập nhật thông tin khách hàng
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    
    // Xóa khách hàng
    void deleteCustomer(Long id);
    
    // Tìm khách hàng theo tên (tìm gần đúng)
    List<CustomerDTO> searchCustomersByName(String name);
    
    // Lấy danh sách tất cả khách hàng
    List<CustomerDTO> getAllCustomers(Long id, String phone, String idNumber);
    
    // Lấy danh sách khách hàng đã từng đặt phòng
    List<CustomerDTO> getCustomersWithBookingHistory();
} 