package com.pplflw.employee;

import com.pplflw.employee.dto.EmployeeUpdateDto;
import com.pplflw.employee.model.Employee;
import com.pplflw.employee.model.EmployeeEvent;
import com.pplflw.employee.model.EmployeeState;

public interface TestData {

    String NAME = "Anton";
    String NAME2 = "Anton2";
    String EMPLOYEE_ID = "testID";
    String NOT_EXISTING_EMPLOYEE_ID = "notExistingId";
    Employee VALID_EMPLOYEE_ANTON_WITHOUT_ID = new Employee(NAME);
    Employee VALID_EMPLOYEE_ANTON = new Employee(EMPLOYEE_ID, NAME);
    Employee VALID_EMPLOYEE_ANTON2 = new Employee(EMPLOYEE_ID, NAME2);
    Employee VALID_EMPLOYEE_ANTON_WITH_ADDED_STATE = new Employee(EMPLOYEE_ID, NAME, EmployeeState.ADDED);
    Employee VALID_EMPLOYEE_ANTON_WITH_IN_CHECK_STATE = new Employee(EMPLOYEE_ID, NAME, EmployeeState.IN_CHECK);
    EmployeeUpdateDto NOT_EXISTING_EMPLOYEE_CHECK_EVENT = new EmployeeUpdateDto(NOT_EXISTING_EMPLOYEE_ID, EmployeeEvent.CHECK);
}
