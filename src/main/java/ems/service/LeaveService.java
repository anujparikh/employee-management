package ems.service;


import ems.dao.LeaveDAO;
import ems.domain.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LeaveService {

    @Autowired
    private LeaveDAO leaveDAO;

    public List<Leave> findByStartDateBetweenForEmployeeId(LocalDate startDate, LocalDate endDate, long employeeId) {
        return leaveDAO.findByStartDateBetween(startDate, endDate).parallelStream().filter(leave -> leave.getEmployee().getId() == employeeId).collect(Collectors.toList());
    }
}
