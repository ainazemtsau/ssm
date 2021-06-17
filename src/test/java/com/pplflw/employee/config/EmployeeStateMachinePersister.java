package com.pplflw.employee.config;

import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;

import java.util.HashMap;

public class EmployeeStateMachinePersister implements StateMachinePersist<EmployeeState, EmployeeEvent, String> {
    private final HashMap<String, StateMachineContext<EmployeeState, EmployeeEvent>> contexts = new HashMap<>();

    @Override
    public void write(final StateMachineContext<EmployeeState, EmployeeEvent> context, String contextObj) {
        contexts.put(contextObj, context);
    }

    @Override
    public StateMachineContext<EmployeeState, EmployeeEvent> read(final String contextObj) {
        return contexts.get(contextObj);
    }
}
