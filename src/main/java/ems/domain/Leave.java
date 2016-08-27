package ems.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "leave_tbl")
@NoArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @NotNull
    private Employee employee;

    @NotNull
    private Integer noOfDays;

    private String approvalStatus;
    private String autoDeducted;
    private String teamId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "leave_employee_map",
            joinColumns = @JoinColumn(name = "leave_id"),
            inverseJoinColumns = @JoinColumn(name = "approver_employee_id")
    )
    private Set<Employee> approverEmployeeIdList;

    public Leave(long id) {
        this.id = id;
    }

    public Leave(LocalDate startDate, Integer noOfDays, Employee employee, Set<Employee> approverEmployeeIdList) {
        this(startDate, noOfDays, employee, "Y", approverEmployeeIdList);
    }

    public Leave(LocalDate startDate, Integer noOfDays, Employee employee, String autoDeducted, Set<Employee> approverEmployeeIdList) {
        this.noOfDays = noOfDays;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(noOfDays);
        this.employee = employee;
        this.autoDeducted = autoDeducted;
        this.approvalStatus = "P";
        this.teamId = employee.getTeamId();
        this.approverEmployeeIdList = approverEmployeeIdList;
    }
}
