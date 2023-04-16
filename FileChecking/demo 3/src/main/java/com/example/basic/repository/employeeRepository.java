package com.example.basic.repository;

import com.example.basic.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface employeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT * FROM new_employees s WHERE s.firstname like %:keyword% or s.lastname like %:keyword%", nativeQuery = true)
    List<Employee> findByKeyword(@Param("keyword") String keyword);


}
