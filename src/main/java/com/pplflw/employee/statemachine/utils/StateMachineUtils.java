package com.pplflw.employee.statemachine.utils;

import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;

public class StateMachineUtils {

    public static final String EMPLOYEE = "Employee";
    public static final String UPDATE_INFO = "updateInfo";

    public Employee getEmployee(StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        return (Employee) stateMachine.getExtendedState().getVariables().get(EMPLOYEE);
    }

    public Object putEmployee(StateMachine<EmployeeState, EmployeeEvent> stateMachine, Employee employee) {
        return stateMachine.getExtendedState().getVariables().put(EMPLOYEE, employee);
    }

    public Object putUpdateMetaInfo(StateMachine<EmployeeState, EmployeeEvent> stateMachine, EmployeeUpdateDto updateInfo) {
        return stateMachine.getExtendedState().getVariables().put(UPDATE_INFO, updateInfo);
    }

    public EmployeeUpdateDto getUpdateMetaInfo(StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        return (EmployeeUpdateDto) stateMachine.getExtendedState().getVariables().get(UPDATE_INFO);
    }

    public EmployeeState getTargetState(StateContext<EmployeeState, EmployeeEvent> stateContext) {
        return stateContext.getTarget().getId();
    }
}
