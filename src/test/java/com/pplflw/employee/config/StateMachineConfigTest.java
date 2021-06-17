package com.pplflw.employee.config;

import com.pplflw.employee.TestData;
import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.statemachine.utils.StateMachineUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.Matchers.hasEntry;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StateMachineConfigTest {

    @Autowired
    private StateMachineFactory<EmployeeState, EmployeeEvent> factory;


    @Autowired
    private StateMachineUtils stateMachineUtils;

    @Test
    public void testStateMachine() throws Exception {
        StateMachine<EmployeeState, EmployeeEvent> machine = factory.getStateMachine();
        machine.getExtendedState().getVariables().put("Employee", TestData.VALID_EMPLOYEE_ANTON);
        stateMachineUtils.putUpdateMetaInfo(machine, new EmployeeUpdateDto(TestData.VALID_EMPLOYEE_ANTON.getId(), EmployeeEvent.ADD));
        StateMachineTestPlan<EmployeeState, EmployeeEvent> plan =
                StateMachineTestPlanBuilder.<EmployeeState, EmployeeEvent>builder()
                        .defaultAwaitTime(2)
                        .stateMachine(machine)
                        .step()
                        .expectStates(EmployeeState.INITIAL)
                        .and()
                        .step()
                        .sendEvent(EmployeeEvent.ADD)
                        .expectState(EmployeeState.ADDED)
                        .expectStateChanged(1)
                        .expectVariableWith(hasEntry("Employee", new Employee(TestData.VALID_EMPLOYEE_ANTON.getId(), TestData.VALID_EMPLOYEE_ANTON.getName(), EmployeeState.ADDED)))
                        .and()
                        .step()
                        .sendEvent(EmployeeEvent.CHECK)
                        .expectState(EmployeeState.IN_CHECK)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(EmployeeEvent.APPROVE)
                        .expectState(EmployeeState.APPROVED)
                        .expectStateChanged(1)
                        .and()
                        .step()
                        .sendEvent(EmployeeEvent.ACTIVATE)
                        .expectState(EmployeeState.ACTIVE)
                        .expectStateChanged(1)
                        .expectStateMachineStopped(1)
                        .and()
                        .build();
        plan.test();
    }
}
