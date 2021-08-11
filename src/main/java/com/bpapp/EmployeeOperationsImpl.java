package com.bpapp;

import com.bpapp.entiy.Employee;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class EmployeeOperationsImpl implements EmployeeOperations {

    private final Map<Integer, Integer> salaryMap;
    private final List<Employee> allEmpList;

    public EmployeeOperationsImpl() {

        allEmpList = Arrays.asList(new Employee(1, "name1"), new Employee(2, "name2"), new Employee(3, "name3"),
                new Employee(4, "name4"), new Employee(5, "name5"), new Employee(6, "name6"),
                new Employee(7, "name7"), new Employee(8, "name8"), new Employee(9, "name9"),
                new Employee(10, "name10"));

        salaryMap = Map.of(1, 2200, 2, 2800, 3, 4124, 4, 1111, 5, 1547, 6, 1011, 7, 2465, 8, 4975,
                9, 153, 10, 2345);
    }

    @Override
    public List<Employee> hiredEmployees() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allEmpList;
    }

    @Override
    public Integer getSalary(Integer hiredEmployeeId) {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return salaryMap.get(hiredEmployeeId);
    }
}
