package com.pplflw.employee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pplflw.employee.TestData;
import com.pplflw.employee.config.TestApplicationConfiguration;
import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.service.EmployeeStateMachineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(TestApplicationConfiguration.class)
@AutoConfigureMockMvc
class EmployeeControllerTest {

    public static final String URL_TEMPLATE = "/employee";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeStateMachineService stateMachineService;
    @Autowired
    private StateMachinePersister<EmployeeState, EmployeeEvent, String> persister;


    @Test
    public void whenAddUserShouldAddEmployeeToDatabaseAndReturnThisUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestContentForCreateUser())).andExpect(status().isOk());
        Employee responseEmployee = getEmployeeFromResultAction(resultActions);
        StateMachine<EmployeeState, EmployeeEvent> machine = getStateMachine(responseEmployee);
        checkStateOfEmployeeAndStateMachine(responseEmployee, machine, EmployeeState.ADDED);
    }


    @Test
    public void updateEmployeeStatus() throws Exception {
        ResultActions resultActionsOfCreateEmployee = this.mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRequestContentForCreateUser())).andExpect(status().isOk());
        Employee responseEmployee = getEmployeeFromResultAction(resultActionsOfCreateEmployee);


        checkUpdateStatus(responseEmployee, EmployeeEvent.CHECK, EmployeeState.IN_CHECK);
        checkUpdateStatus(responseEmployee, EmployeeEvent.APPROVE, EmployeeState.APPROVED);
        checkUpdateStatus(responseEmployee, EmployeeEvent.ACTIVATE, EmployeeState.ACTIVE);
    }

    private void checkUpdateStatus(Employee responseEmployee,EmployeeEvent sendEvent, EmployeeState checkState) throws Exception {
        String requestBody = getRequestContentForUpdateStatusUserToCheck(responseEmployee.getId(), sendEvent);
        ResultActions resultActions = this.mockMvc.perform(put(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());

        Employee response = getEmployeeFromResultAction(resultActions);
        StateMachine<EmployeeState, EmployeeEvent> machine = getStateMachine(responseEmployee);
        checkStateOfEmployeeAndStateMachine(response, machine, checkState);
        assertThat(machine.getExtendedState().get("Employee", Employee.class).getState(), is(checkState));

        this.mockMvc.perform(put(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andExpect(status().isOk());
    }


    private StateMachine<EmployeeState, EmployeeEvent> getStateMachine(Employee responseEmployee) throws Exception {
        StateMachine<EmployeeState, EmployeeEvent> machine = stateMachineService.getRestoredStateMachine(responseEmployee.getId());
        machine = persister.restore(machine, responseEmployee.getId());
        return machine;
    }

    private void checkStateOfEmployeeAndStateMachine(Employee employee, StateMachine<EmployeeState, EmployeeEvent> machine, EmployeeState state) {
        assertThat(machine.getState().getId(), is(state));
        assertThat(employee.getState(), is(state));
        assertThat(employee.getState(), is(machine.getState().getId()));
    }

    private Employee getEmployeeFromResultAction(ResultActions resultActions) throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, Employee.class);
    }

    private String getRequestContentForCreateUser() throws JsonProcessingException {
        return objectMapper.writeValueAsString(TestData.VALID_EMPLOYEE_ANTON_WITHOUT_ID);
    }

    private String getRequestContentForUpdateStatusUserToCheck(String userId, EmployeeEvent event) throws JsonProcessingException {

        return objectMapper.writeValueAsString(new EmployeeUpdateDto(userId, event));
    }

    private String getRequestContentForUpdateNonExistingUser() throws JsonProcessingException {
        return objectMapper.writeValueAsString(TestData.NOT_EXISTING_EMPLOYEE_CHECK_EVENT);
    }
}
