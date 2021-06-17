package com.pplflw.employee.config;

import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@TestConfiguration
public class TestApplicationConfiguration {

    @Bean
    @Primary
    public StateMachinePersister<EmployeeState, EmployeeEvent, String> testPersister() {
        return new DefaultStateMachinePersister<>(new EmployeeStateMachinePersister());
    }


}
