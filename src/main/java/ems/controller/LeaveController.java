package ems.controller;

import ems.dao.EmployeeDAO;
import ems.dao.LeaveDAO;
import ems.domain.Employee;
import ems.domain.Leave;
import ems.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Controller
@RequestMapping("/leavecontroller")
public class LeaveController {

    private static final String DATE_PATTERN = "MM-dd-yyyy";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private final LeaveDAO leaveDAO;
    private final EmployeeDAO employeeDAO;
    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveDAO leaveDAO, EmployeeDAO employeeDAO, LeaveService leaveService) {
        this.leaveDAO = leaveDAO;
        this.employeeDAO = employeeDAO;
        this.leaveService = leaveService;
    }

    /**
     *
     * @param startDate - leave start date
     * @param noOfDays - total number of leaves applied
     * @param employeeId - employee id applying for leave
     * @return - New Created Leave
     */
    @RequestMapping("/create")
    @ResponseBody
    public Leave create(@RequestParam(name = "startDate", required = false) String startDate,
                         int noOfDays,
                         Long employeeId) {
        Leave leave;
        try {
            LocalDate inputStartDate = startDate == null ? LocalDate.now() : LocalDate.parse(startDate, formatter);
            Employee retrievedEmployee = employeeDAO.findOne(employeeId);
            leave = new Leave(inputStartDate, noOfDays, retrievedEmployee, "N", employeeDAO.findByTeamId(retrievedEmployee.getTeamId()));
            leaveDAO.save(leave);
        } catch (Exception e) {
            return null;
        }
        return leave;
    }

    /**
     *
     * @param id - leave id
     * @param startDate - modified start date (optional)
     * @param noOfDays - modified no of days (optional)
     * @return - updated leave
     */
    @RequestMapping("/update/{id}")
    @ResponseBody
    public Leave update(@PathVariable Long id,
                         @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) Integer noOfDays) {
        Leave retrievedLeave, updatedLeave;
        try {
            retrievedLeave = leaveDAO.findOne(id);
            LocalDate modifiedStartDate = startDate == null ? retrievedLeave.getStartDate() : LocalDate.parse(startDate, formatter);
            Integer modifiedNoOfDays = noOfDays == null ? retrievedLeave.getNoOfDays() : noOfDays;
            retrievedLeave.setStartDate(modifiedStartDate);
            retrievedLeave.setNoOfDays(modifiedNoOfDays);
            retrievedLeave.setEndDate(modifiedStartDate.plusDays(modifiedNoOfDays));
            updatedLeave = leaveDAO.save(retrievedLeave);
        } catch (Exception e) {
            return null;
        }
        return updatedLeave;
    }

    /**
     *
     * @param id - leave id for leave to be deleted
     * @return - Boolean - successful or failure
     */
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Boolean delete(@PathVariable Long id) {
        try {
            leaveDAO.delete(new Leave(id));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param startDate - start date of the range of leaves to be retrieved
     * @param noOfDays - total no of days to calculate end date of range
     * @param employeeId - employee id for which leaves needs to be fetched
     * @return - Set of retrieved leaves
     */
    @RequestMapping("/range")
    @ResponseBody
    public Set<Leave> range(String startDate,
                        Integer noOfDays,
                        Long employeeId) {
        Set<Leave> finalListOfLeaves;
        try {
            LocalDate inputStartDate = LocalDate.parse(startDate, formatter);
            LocalDate calcEndDate = inputStartDate.plusDays(noOfDays);
            finalListOfLeaves = leaveService.findByStartDateBetweenForEmployeeId(inputStartDate, calcEndDate, employeeId);
            finalListOfLeaves.forEach(i -> {
                System.out.println("No of days for " + i.getEmployee().getFirstName() + " is: " + i.getNoOfDays());
            });
        } catch (Exception e) {
            return null;
        }
        return finalListOfLeaves;
    }

    /**
     *
     * @param teamId - team id for which all the leaves are to be retrieved
     * @return - Set of leaves for particular team id
     */
    @RequestMapping("/{teamId}/team-list")
    @ResponseBody
    public String findByTeamId(@PathVariable String teamId) {
        Set<Leave> retrievedLeaveList;
        try {
            retrievedLeaveList = leaveDAO.findByTeamId(teamId);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Total Retrieved Leaves for team with id " + teamId + " is " + retrievedLeaveList.size();
    }

    /**
     *
     * @param leaveId - leave id for which approvers list needs to be retrieved
     * @return - Set of Employees who are approvers for the leave id passed
     */
    @RequestMapping("/{leaveId}/approver-list")
    @ResponseBody
    public Set<Employee> findApproverListByLeaveId(@PathVariable Long leaveId) {
        Set<Employee> retrievedApproverSet;
        try {
            retrievedApproverSet = leaveService.findApproverSetByLeaveId(leaveId);
        } catch (Exception e) {
            return null;
        }
        return retrievedApproverSet;
    }

    /**
     *
     * @param approverId - approved id for which leaves to be retrieved for approval
     * @return - Set of Leaves for that approved id
     */
    @RequestMapping("/assigned/{approverId}")
    @ResponseBody
    public Set<Leave> findByApproverEmployeeId(@PathVariable Long approverId) {
        Set<Leave> retrievedLeaveSet;
        try {
            retrievedLeaveSet = leaveService.findLeaveSetByEmployeeId(approverId);
        } catch (Exception e) {
            return null;
        }
        return retrievedLeaveSet;
    }
}
