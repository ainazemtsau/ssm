package com.pplflw.employee.service;

import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.statemachine.utils.StateMachineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

@Service
public class EmployeeStateMachineServiceImpl implements EmployeeStateMachineService {

    Logger log = LoggerFactory.getLogger(EmployeeStateMachineServiceImpl.class);
    private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
    private final StateMachineUtils stateMachineUtils;
    private final StateMachinePersister<EmployeeState, EmployeeEvent, String> persister;

    public EmployeeStateMachineServiceImpl(StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory,
                                           StateMachinePersister<EmployeeState, EmployeeEvent, String> persister,
                                           StateMachineUtils stateMachineUtils) {
        this.stateMachineFactory = stateMachineFactory;
        this.stateMachineUtils = stateMachineUtils;
        this.persister = persister;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        StateMachine<EmployeeState, EmployeeEvent> stateMachine = sendAddEvent(employee);
        return savedStateMachine(stateMachine);
    }

    @Override
    public Employee updateEmployeeState(EmployeeUpdateDto dto) {
        StateMachine<EmployeeState, EmployeeEvent> stateMachine = sendUpdateEvent(dto);
        return savedStateMachine(stateMachine);
    }

    @Override
    public StateMachine<EmployeeState, EmployeeEvent> getRestoredStateMachine(String machineId) {
        StateMachine<EmployeeState, EmployeeEvent> stateMachine = this.stateMachineFactory.getStateMachine();
        try {
            stateMachine = this.persister.restore(stateMachine, machineId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stateMachine;
    }

    private Employee savedStateMachine(StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        Employee savedEmployee = this.stateMachineUtils.getEmployee(stateMachine);
        try {
            this.persister.persist(stateMachine, savedEmployee.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return this.stateMachineUtils.getEmployee(stateMachine);
    }

    private StateMachine<EmployeeState, EmployeeEvent> sendAddEvent(Employee employee) {
        StateMachine<EmployeeState, EmployeeEvent> stateMachine = this.stateMachineFactory.getStateMachine();
        this.stateMachineUtils.putEmployee(stateMachine, employee);
        stateMachine.sendEvent(EmployeeEvent.ADD);
        return stateMachine;
    }


    private StateMachine<EmployeeState, EmployeeEvent> sendUpdateEvent(EmployeeUpdateDto employeeUpdateDto) {
        StateMachine<EmployeeState, EmployeeEvent> stateMachine = getRestoredStateMachine(employeeUpdateDto.getEmployeeId());
        this.stateMachineUtils.putUpdateMetaInfo(stateMachine, employeeUpdateDto);
        stateMachine.sendEvent(employeeUpdateDto.getEvent());
        return stateMachine;
    }

}
