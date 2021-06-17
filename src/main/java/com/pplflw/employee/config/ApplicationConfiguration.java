package com.pplflw.employee.config;

import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;
import com.pplflw.employee.service.EmployeeService;
import com.pplflw.employee.statemachine.action.ActionFactory;
import com.pplflw.employee.statemachine.utils.StateMachineUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.statemachine.data.redis.RedisPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.statemachine.data.redis.RedisStateMachineRepository;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableRedisRepositories()
public class ApplicationConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public StateMachineRuntimePersister<EmployeeState, EmployeeEvent, String> stateMachineRuntimePersister(
            RedisStateMachineRepository jpaStateMachineRepository) {
        return new RedisPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("redis");
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public StateMachinePersister<EmployeeState, EmployeeEvent, String> persister(
            StateMachineRuntimePersister<EmployeeState, EmployeeEvent, String> stateMachineRuntimePersister) {
        return new RedisStateMachinePersister<>(stateMachineRuntimePersister);
    }

    @Bean
    public StateMachineUtils stateMachineUtils() {
        return new StateMachineUtils();
    }


    @Bean
    public ActionFactory factory(
            EmployeeService employeeService, StateMachineUtils utils) {
        return new ActionFactory(employeeService, utils);
    }

}
