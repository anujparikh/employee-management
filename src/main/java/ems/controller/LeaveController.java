package ems.controller;

import ems.domain.Employee;
import ems.domain.Leave;
import ems.dao.EmployeeDAO;
import ems.dao.LeaveDAO;
import ems.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/leavecontroller")
public class LeaveController {

    private static final String DATE_PATTERN = "MM-dd-yyyy";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Autowired
    private LeaveDAO leaveDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private LeaveService leaveService;

    @RequestMapping("/create")
    @ResponseBody
    public String create(@RequestParam(name = "startDate", required = false) String startDate,
                         int noOfDays,
                         long employeeId) {
        String leaveId = "";
        Leave leave;
        try {
            LocalDate inputStartDate = startDate == null ? LocalDate.now() : LocalDate.parse(startDate, formatter);
            Employee retrievedEmployee = employeeDAO.findOne(employeeId);
            leave = new Leave(inputStartDate, noOfDays, retrievedEmployee, "N", employeeDAO.findByTeamId(retrievedEmployee.getTeamId()));
            leaveDAO.save(leave);
            leaveId = String.valueOf(leave.getId());
        } catch (Exception e) {
            return "Error creating the leave: " + e.toString();
        }
        return "Leave successfully created with id = " + leaveId;
    }

    @RequestMapping("/update/{id}")
    @ResponseBody
    public String update(@PathVariable long id,
                         @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) Integer noOfDays) {
        Leave retrievedLeave;
        try {
            retrievedLeave = leaveDAO.findOne(id);
            LocalDate modifiedStartDate = startDate == null ? retrievedLeave.getStartDate() : LocalDate.parse(startDate, formatter);
            Integer modifiedNoOfDays = noOfDays == null ? retrievedLeave.getNoOfDays() : noOfDays;
            retrievedLeave.setStartDate(modifiedStartDate);
            retrievedLeave.setNoOfDays(modifiedNoOfDays);
            retrievedLeave.setEndDate(modifiedStartDate.plusDays(modifiedNoOfDays));
            leaveDAO.save(retrievedLeave);
        } catch (Exception e) {
            return "Error creating the leave: " + e.toString();
        }
        return "updated leave successfully with id = " + id;
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable long id) {
        try {
            leaveDAO.delete(new Leave(id));
        } catch (Exception e) {
            return "Error deleting the leave: " + e.toString();
        }
        return "Deleted leave successfully with id = " + id;
    }

    @RequestMapping("/range")
    @ResponseBody
    public String range(String startDate,
                         Integer noOfDays,
                         long employeeId) {
        List<Leave> finalListOfLeaves;
        try {
            LocalDate inputStartDate = LocalDate.parse(startDate, formatter);
            LocalDate calcEndDate = inputStartDate.plusDays(noOfDays);
             finalListOfLeaves = leaveService.findByStartDateBetweenForEmployeeId(inputStartDate, calcEndDate, employeeId);
        } catch (Exception e) {
            return "Error deleting the leave: " + e.toString();
        }
        return "leave successfully with size = " + finalListOfLeaves.size();
    }

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
}
