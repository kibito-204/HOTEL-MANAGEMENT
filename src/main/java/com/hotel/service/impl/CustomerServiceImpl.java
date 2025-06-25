package com.hotel.service.impl;

import com.hotel.dto.CustomerDTO;
import com.hotel.entity.Customer;
import com.hotel.entity.Reservation;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.exception.ValidationException;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.ReservationRepository;
import com.hotel.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        logger.info("Creating new customer with phone: {}", customerDTO.getPhoneNumber());
        try {
            // Kiểm tra số điện thoại đã tồn tại
            if (customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber()).isPresent()) {
                logger.warn("Phone number already exists: {}", customerDTO.getPhoneNumber());
                throw new ValidationException("Số điện thoại đã được đăng ký");
            }

            // Kiểm tra CMND/CCCD đã tồn tại
            if(customerRepository.findByIdNumber(customerDTO.getIdNumber()).isPresent()){
                throw new ValidationException("Số CMND/CCCD đã được đăng ký");
            }
            
            // Kiểm tra email đã tồn tại (nếu có)
            if (customerDTO.getEmail() != null && 
                customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
                logger.warn("Email already exists: {}", customerDTO.getEmail());
                throw new ValidationException("Email đã được đăng ký");
            }

            Customer customer = new Customer();
            BeanUtils.copyProperties(customerDTO, customer);
            customer = customerRepository.save(customer);
            
            CustomerDTO result = new CustomerDTO();
            BeanUtils.copyProperties(customer, result);
            logger.info("Successfully created customer with ID: {}", customer.getId());
            return result;
        } catch (Exception e) {
            logger.error("Error creating customer: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo khách hàng: " + e.getMessage());
        }
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        logger.info("Updating customer with ID: {}", id);
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));

            // Kiểm tra số điện thoại đã tồn tại
            customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber())
                    .ifPresent(existingCustomer -> {
                        if (!existingCustomer.getId().equals(id)) {
                            logger.warn("Phone number already exists: {}", customerDTO.getPhoneNumber());
                            throw new ValidationException("Số điện thoại đã được đăng ký");
                        }
                    });

            // Kiểm tra CMND/CCCD đã tồn tại
            customerRepository.findByIdNumber(customerDTO.getIdNumber())
                    .ifPresent(existingCustomer -> {
                        if (!existingCustomer.getId().equals(id)) {
                            logger.warn("ID number already exists: {}", customerDTO.getIdNumber());
                            throw new ValidationException("Số CMND/CCCD đã được đăng ký");
                        }
                    });

            // Kiểm tra email đã tồn tại (nếu có)
            if (customerDTO.getEmail() != null) {
                customerRepository.findByEmail(customerDTO.getEmail())
                        .ifPresent(existingCustomer -> {
                            if (!existingCustomer.getId().equals(id)) {
                                logger.warn("Email already exists: {}", customerDTO.getEmail());
                                throw new ValidationException("Email đã được đăng ký");
                            }
                        });
            }

            BeanUtils.copyProperties(customerDTO, customer, "id");
            customer = customerRepository.save(customer);
            
            CustomerDTO result = new CustomerDTO();
            BeanUtils.copyProperties(customer, result);
            logger.info("Successfully updated customer with ID: {}", id);
            return result;
        } catch (Exception e) {
            logger.error("Error updating customer: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể cập nhật khách hàng: " + e.getMessage());
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        logger.info("Deleting customer with ID: {}", id);
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));

            // Kiểm tra xem khách hàng có lịch sử đặt phòng không
            List<Reservation> reservations = reservationRepository.findByCustomerId(id);
            if (!reservations.isEmpty()) {
                logger.warn("Cannot delete customer with ID {}: has booking history", id);
                throw new ValidationException("Không thể xóa khách hàng đã có lịch sử đặt phòng");
            }

            customerRepository.delete(customer);
            logger.info("Successfully deleted customer with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting customer: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể xóa khách hàng: " + e.getMessage());
        }
    }

   
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomersByName(String name) {
        logger.info("Searching customers by name: {}", name);
        try {
            List<CustomerDTO> customers = customerRepository.findByNameStartingWithIgnoreCase(name).stream()
                    .map(customer -> {
                        CustomerDTO customerDTO = new CustomerDTO();
                        BeanUtils.copyProperties(customer, customerDTO);
                        
                        // Kiểm tra lịch sử đặt phòng
                        List<Reservation> reservations = reservationRepository.findByCustomerId(customer.getId());
                        customerDTO.setHasBookingHistory(!reservations.isEmpty());
                        
                        return customerDTO;
                    })
                    .collect(Collectors.toList());
            
            logger.info("Found {} customers matching name: {}", customers.size(), name);
            return customers;
        } catch (Exception e) {
            logger.error("Error searching customers by name: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi tìm khách hàng theo tên: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
        public List<CustomerDTO> getAllCustomers(Long id, String phone, String idNumber) {
        return customerRepository.findAll().stream()
        .filter(customer -> id == null || customer.getId() == id)
        .filter(customer -> phone == null || customer.getPhoneNumber().equals(phone))
        .filter(customer -> idNumber == null || customer.getIdNumber().equals(idNumber))
        .map(customer -> {
            CustomerDTO customerDTO = new CustomerDTO();
            BeanUtils.copyProperties(customer, customerDTO);
            return customerDTO;
        })
        .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersWithBookingHistory() {
        logger.info("Getting customers with booking history");
        try {
            List<CustomerDTO> customers = customerRepository.findCustomersWithBookings().stream()
                    .map(customer -> {
                        CustomerDTO customerDTO = new CustomerDTO();
                        BeanUtils.copyProperties(customer, customerDTO);
                        customerDTO.setHasBookingHistory(true);
                        return customerDTO;
                    })
                    .collect(Collectors.toList());
            
            logger.info("Found {} customers with booking history", customers.size());
            return customers;
        } catch (Exception e) {
            logger.error("Error getting customers with booking history: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi lấy danh sách khách hàng có lịch sử đặt phòng: " + e.getMessage());
        }
    }
} 