package ems.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "leave_tbl")
@NoArgsConstructor
@Component
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
    private ArrayList<Employee> approverEmployeeIdList;

    public Leave(LocalDate startDate,
                 LocalDate endDate,
                 Employee employee,
                 Integer noOfDays,
                 String approvalStatus,
                 String autoDeducted,
                 String teamId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.employee = employee;
        this.noOfDays = noOfDays;
        this.approvalStatus = approvalStatus;
        this.autoDeducted = autoDeducted;
        this.teamId = teamId;
    }
}
