package com.pplflw.employee.service;

import com.pplflw.employee.TestData;
import com.pplflw.employee.exception.EmployeeException;
import com.pplflw.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeServiceImplTest {

    EmployeeRepository repository = mock(EmployeeRepository.class);
    EmployeeService service = new EmployeeServiceImpl(repository);

    @Test
    void whenUpdateNonExistsEmployeeShouldThrowException() {
        when(repository.findById(TestData.VALID_EMPLOYEE_ANTON.getId())).thenReturn(Optional.empty());
        assertThrows(EmployeeException.class, () -> service.updateEmployee(TestData.VALID_EMPLOYEE_ANTON));
    }
}
