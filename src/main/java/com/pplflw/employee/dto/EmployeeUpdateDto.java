package com.pplflw.employee.dto;

import com.pplflw.employee.model.EmployeeEvent;

public class EmployeeUpdateDto {
    private String employeeId;
    private EmployeeEvent event;

    public EmployeeUpdateDto(String employeeId, EmployeeEvent event) {
        this.employeeId = employeeId;
        this.event = event;
    }

    public EmployeeUpdateDto() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public EmployeeEvent getEvent() {
        return event;
    }

    public void setEvent(EmployeeEvent event) {
        this.event = event;
    }
}
