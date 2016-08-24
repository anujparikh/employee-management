package org.management.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private String employeeId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Team team;
    @NotNull
    private String role;
    private Employee manager;

    public Employee(String firstName, String lastName, String employeeId, Team team, String role, Employee manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.team = team;
        this.role = role;
        this.manager = manager;
    }
}
