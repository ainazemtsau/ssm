package com.pplflw.employee.statemachine.action;

import com.pplflw.employee.TestData;
import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.service.EmployeeService;
import com.pplflw.employee.statemachine.utils.StateMachineUtils;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateEmployeeStateActionTest {

    private final StateMachineUtils utils = mock(StateMachineUtils.class);
    private final EmployeeService service = mock(EmployeeService.class);
    private final StateContext<EmployeeState, EmployeeEvent> stateContext = mock(StateContext.class);
    private final UpdateEmployeeStateAction action = new UpdateEmployeeStateAction(service, utils);

    @Test
    void execute() {
        EmployeeUpdateDto updateDto = new EmployeeUpdateDto(TestData.EMPLOYEE_ID, EmployeeEvent.CHECK);
        when(utils.getUpdateMetaInfo(any())).thenReturn(updateDto);
        when(utils.getTargetState(any())).thenReturn(EmployeeState.IN_CHECK);
        when(service.getEmployeeById(TestData.EMPLOYEE_ID)).thenReturn(Optional.of(TestData.VALID_EMPLOYEE_ANTON));
        action.execute(stateContext);
        verify(utils, times(1)).putEmployee(any(), eq(TestData.VALID_EMPLOYEE_ANTON_WITH_IN_CHECK_STATE));
    }
}
