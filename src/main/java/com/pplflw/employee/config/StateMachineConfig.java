package com.pplflw.employee.config;

import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.statemachine.action.ActionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<EmployeeState, EmployeeEvent> {

    private final ActionFactory factory;

    public StateMachineConfig(ActionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void configure(final StateMachineStateConfigurer<EmployeeState, EmployeeEvent> states) throws Exception {
        states
                .withStates()
                .initial(EmployeeState.INITIAL)
                .end(EmployeeState.ACTIVE)
                .states(EnumSet.allOf(EmployeeState.class));

    }

    @Override
    public void configure(final StateMachineConfigurationConfigurer<EmployeeState, EmployeeEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<EmployeeState, EmployeeEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(EmployeeState.INITIAL)
                .target(EmployeeState.ADDED)
                .event(EmployeeEvent.ADD)
                .action(factory.getCreateEmployee())
                .and()

                .withExternal()
                .source(EmployeeState.ADDED)
                .target(EmployeeState.IN_CHECK)
                .event(EmployeeEvent.CHECK)
                .action(factory.getUpdateStateEmployee())

                .and()
                .withExternal()
                .source(EmployeeState.IN_CHECK)
                .target(EmployeeState.APPROVED)
                .event(EmployeeEvent.APPROVE)
                .action(factory.getUpdateStateEmployee())

                .and()
                .withExternal()
                .source(EmployeeState.APPROVED)
                .target(EmployeeState.ACTIVE)
                .action(factory.getUpdateStateEmployee())
                .event(EmployeeEvent.ACTIVATE);

    }
}
