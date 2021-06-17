package com.pplflw.employee.service;

import com.pplflw.employee.model.Employee;

import java.util.Optional;

public interface EmployeeService {

    Employee addNewEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    Optional<Employee> getEmployeeById(String id);
}
