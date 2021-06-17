package com.pplflw.employee.statemachine.action;

import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.service.EmployeeService;
import com.pplflw.employee.statemachine.utils.StateMachineUtils;
import org.springframework.statemachine.action.Action;

public class ActionFactory {

    private final Action<EmployeeState, EmployeeEvent> createEmployee;
    private final Action<EmployeeState, EmployeeEvent> updateStateEmployee;

    public ActionFactory(EmployeeService employeeService, StateMachineUtils utils) {
        this.createEmployee = new CreateEmployeeAction(employeeService, utils);
        this.updateStateEmployee = new UpdateEmployeeStateAction(employeeService, utils);
    }

    public Action<EmployeeState, EmployeeEvent> getCreateEmployee() {
        return createEmployee;
    }

    public Action<EmployeeState, EmployeeEvent> getUpdateStateEmployee() {
        return updateStateEmployee;
    }
}
