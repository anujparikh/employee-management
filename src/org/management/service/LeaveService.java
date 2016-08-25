package org.management.service;

import org.management.domain.Leave;

import java.util.List;

public interface LeaveService extends EmployeeDao {

    List<Leave> retrieveLeavesOnEmployeeId(String employeeId);

    List<Leave> retrieveLeavesPendingForApproval(String employeeId);

    List<Leave> retrieveLeavesOnStatusEmployeeId(String employeeId, String status);
}
