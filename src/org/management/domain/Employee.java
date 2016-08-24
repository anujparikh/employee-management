package org.management.domain;

import lombok.Data;

@Data
public class Employee {

    private String firstName;
    private String lastName;
    private String employeeId;
    private String teamId;
    private String role;
    private String managerId;

    public Employee(String firstName, String lastName, String employeeId, String teamId, String role, String managerId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.teamId = teamId;
        this.role = role;
        this.managerId = managerId;
    }
}
