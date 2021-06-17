package com.pplflw.employee.statemachine.action;

import com.pplflw.employee.TestData;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.service.EmployeeService;
import com.pplflw.employee.statemachine.utils.StateMachineUtils;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateEmployeeActionTest {
    private final StateMachineUtils utils = mock(StateMachineUtils.class);
    private final EmployeeService service = mock(EmployeeService.class);
    private final StateContext<EmployeeState, EmployeeEvent> stateContext = mock(StateContext.class);
    private final CreateEmployeeAction action = new CreateEmployeeAction(service, utils);

    @Test
    void execute() {
        when(utils.getEmployee(any())).thenReturn(TestData.VALID_EMPLOYEE_ANTON_WITHOUT_ID);
        when(service.addNewEmployee(TestData.VALID_EMPLOYEE_ANTON_WITHOUT_ID)).thenReturn(TestData.VALID_EMPLOYEE_ANTON_WITH_ADDED_STATE);
        action.execute(stateContext);
        verify(utils, times(1)).putEmployee(any(), eq(TestData.VALID_EMPLOYEE_ANTON_WITH_ADDED_STATE));
    }
}
