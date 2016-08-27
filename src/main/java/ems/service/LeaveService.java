package ems.service;


import ems.dao.LeaveDAO;
import ems.domain.Employee;
import ems.domain.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional
@Service
public class LeaveService {

    @Autowired
    private LeaveDAO leaveDAO;

    public Set<Leave> findByStartDateBetweenForEmployeeId(LocalDate startDate, LocalDate endDate, Long employeeId) {
        return leaveDAO.findByStartDateBetween(startDate, endDate).parallelStream().filter(leave -> leave.getEmployee().getId() == employeeId).collect(Collectors.toSet());
    }

    public Set<Employee> findApproverSetByLeaveId(Long leaveId) {
        return leaveDAO.findOne(leaveId).getApproverEmployeeIdList();
    }

    public Set<Leave> findLeaveSetByEmployeeId(Long employeeId) {
        return StreamSupport.stream(leaveDAO.findAll().spliterator(), true).filter(leave -> getApproverIdList(leave).contains(employeeId)).collect(Collectors.toSet());
    }

    private static List<Long> getApproverIdList(Leave input) {
        return input.getApproverEmployeeIdList().parallelStream().map(Employee::getId).collect(Collectors.toList());
    }
}
