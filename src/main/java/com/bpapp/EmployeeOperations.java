package com.bpapp;

import com.bpapp.entiy.Employee;

import java.util.List;

public interface EmployeeOperations {
    List<Employee> hiredEmployees() throws InterruptedException;

    Integer getSalary(Integer hiredEmployeeId) throws InterruptedException;
}
