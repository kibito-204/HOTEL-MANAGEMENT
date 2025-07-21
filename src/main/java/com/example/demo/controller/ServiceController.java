package com.example.demo.controller;

import com.example.demo.dto.HotelServiceDTO;
import com.example.demo.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;
    @GetMapping("")
    public ResponseEntity<List<HotelServiceDTO>> getService(){
        List<HotelServiceDTO> s = serviceService.getService();
        return ResponseEntity.ok(s);
    }
    @PostMapping("/create")
    public ResponseEntity<HotelServiceDTO> createService(@RequestBody HotelServiceDTO dto){
        HotelServiceDTO dto1 = serviceService.createService(dto);
        return new ResponseEntity<>(dto1, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> updateService( @PathVariable Long id){
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
