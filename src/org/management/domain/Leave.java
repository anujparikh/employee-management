package org.management.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "leave")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private String leaveId;
    private Date startDate;
    @NotNull
    private Integer noOfDays;
    @NotNull
    private List<Employee> approvalList;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @NotNull
    private Employee employee;

    private String approvalStatus;
    private String autoDeducted;

    public Leave(String leaveId, Date startDate, Integer noOfDays, Employee employee) {
        this(leaveId, startDate, noOfDays, employee, "Y");
    }

    public Leave(String leaveId, Date startDate, Integer noOfDays, Employee employee, String autoDeducted) {
        this.leaveId = leaveId;
        this.startDate = startDate;
        this.noOfDays = noOfDays;
        this.employee = employee;
        this.autoDeducted = autoDeducted;
        this.approvalStatus = "P";
    }
}
