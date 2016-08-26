package ems.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy="manager")
    private Set<Employee> subordinates = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "leave_employee_map",
            joinColumns = @JoinColumn(name = "approver_employee_id", referencedColumnName = "manager_id"),
            inverseJoinColumns = @JoinColumn(name = "leave_id", referencedColumnName = "id")
    )
    private Set<Leave> leaves;

    public Employee(String firstName, String lastName, String email, String teamId, String role, Employee manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.teamId = teamId;
        this.role = role;
        this.manager = manager;
    }

    public Employee(Long id) {
        this.id = id;
    }
}
