package ems.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "leave_tbl")
@NoArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private int noOfDays;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    @NotNull
    private Employee employee;

    private Date startDate;
    private String approvalStatus;
    private String autoDeducted;
    private String teamId;

    public Leave(long id) {
        this.id = id;
    }

    public Leave(Date startDate, Integer noOfDays, Employee employee) {
        this(startDate, noOfDays, employee, "Y");
    }

    public Leave(Date startDate, Integer noOfDays, Employee employee, String autoDeducted) {
        this.startDate = startDate;
        this.noOfDays = noOfDays;
        this.employee = employee;
        this.autoDeducted = autoDeducted;
        this.approvalStatus = "P";
        this.teamId = employee.getTeamId();
    }
}
