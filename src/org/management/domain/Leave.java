package org.management.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Leave {
    private Date startDate;
    private Integer noOfDays;
    private List<Employee> approvalList;
    private String employeeId;
    private String approvalStatus;
    private String autoDeducted;

    public Leave(Date startDate, Integer noOfDays, String employeeId) {
        this(startDate, noOfDays, employeeId, "Y");
    }

    public Leave(Date startDate, Integer noOfDays, String employeeId, String autoDeducted) {
        this.startDate = startDate;
        this.noOfDays = noOfDays;
        this.employeeId = employeeId;
        this.autoDeducted = autoDeducted;
        this.approvalStatus = "P";
    }
}
