package com.pplflw.employee.service;

import com.pplflw.employee.exception.EmployeeException;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee addNewEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        Optional<Employee> optionalEmployeeFromDb = getEmployeeById(employee.getId());
        Employee employeeFromDb = optionalEmployeeFromDb.orElseThrow(() ->
                new EmployeeException(String.format("Can not find an employee with id '%s'", employee.getId())));
        return repository.save(employeeFromDb);
    }

    @Override
    public Optional<Employee> getEmployeeById(String id) {
        return repository.findById(id);
    }

}
