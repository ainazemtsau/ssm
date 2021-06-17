package com.pplflw.employee.service;

import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import org.springframework.statemachine.StateMachine;

public interface EmployeeStateMachineService {

    Employee createEmployee(Employee employee);

    Employee updateEmployeeState(EmployeeUpdateDto dto);

    StateMachine<EmployeeState, EmployeeEvent> getRestoredStateMachine(String machineId);
}
