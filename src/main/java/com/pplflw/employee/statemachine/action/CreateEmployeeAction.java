package com.pplflw.employee.statemachine.action;

import com.pplflw.employee.model.Employee;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.service.EmployeeService;
import com.pplflw.employee.statemachine.utils.StateMachineUtils;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class CreateEmployeeAction implements Action<EmployeeState, EmployeeEvent> {

    private final EmployeeService employeeService;

    private final StateMachineUtils utils;

    public CreateEmployeeAction(EmployeeService employeeService, StateMachineUtils utils) {
        this.employeeService = employeeService;
        this.utils = utils;
    }

    @Override
    public void execute(StateContext<EmployeeState, EmployeeEvent> stateContext) {
        Employee employee = utils.getEmployee(stateContext.getStateMachine());
        employee.setState(EmployeeState.ADDED);
        Employee savedEmployee = employeeService.addNewEmployee(employee);
        utils.putEmployee(stateContext.getStateMachine(), savedEmployee);
    }

}
