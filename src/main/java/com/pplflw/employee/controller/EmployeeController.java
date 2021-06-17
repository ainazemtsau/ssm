package com.pplflw.employee.controller;

import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.service.EmployeeStateMachineService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeStateMachineService stateMachineService;

    public EmployeeController(EmployeeStateMachineService stateMachineService) {
        this.stateMachineService = stateMachineService;

    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return this.stateMachineService.createEmployee(employee);
    }

    @PutMapping
    public Employee updateEmployeeStatus(@RequestBody EmployeeUpdateDto dto) {
        return this.stateMachineService.updateEmployeeState(dto);
    }
}
