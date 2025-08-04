package com.example.demo.repository;

import com.example.demo.entity.UsedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsedServiceRepository extends JpaRepository<UsedService, Long> {
    @Query("select u.service.name,count(u) from UsedService u group by u.service.name order by count(u) desc")
    List<Object[]> listTopService();
}
