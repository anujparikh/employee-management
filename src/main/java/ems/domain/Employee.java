package ems.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "employee")
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String teamId;

    @NotNull
    private String role;
    private String managerId;

    public Employee(String firstName, String lastName, String email, String teamId, String role, String managerId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.teamId = teamId;
        this.role = role;
        this.managerId = managerId;
    }

    public Employee(long id) {
        this.id = id;
    }
}
