package ems.controllers;

import ems.domain.Leave;
import ems.services.EmployeeService;
import ems.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/leavecontroller")
public class LeaveController {

    private static final String DATE_PATTERN = "MM-dd-yyyy";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/create")
    @ResponseBody
    public String create(@RequestParam(name = "startDate", required = false) String startDate,
                         int noOfDays,
                         long employeeId) {
        String leaveId = "";
        Leave leave;
        try {
            LocalDate inputStartDate = startDate == null ? LocalDate.now() : LocalDate.parse(startDate, formatter);
            leave = new Leave(inputStartDate, noOfDays, employeeService.findOne(employeeId), "N");
            leaveService.save(leave);
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
            retrievedLeave = leaveService.findOne(id);
            LocalDate modifiedStartDate = startDate == null ? retrievedLeave.getStartDate() : LocalDate.parse(startDate, formatter);
            Integer modifiedNoOfDays = noOfDays == null ? retrievedLeave.getNoOfDays() : noOfDays;
            retrievedLeave.setStartDate(modifiedStartDate);
            retrievedLeave.setNoOfDays(modifiedNoOfDays);
            retrievedLeave.setEndDate(modifiedStartDate.plusDays(modifiedNoOfDays));
            leaveService.save(retrievedLeave);
        } catch (Exception e) {
            return "Error creating the leave: " + e.toString();
        }
        return "updated leave successfully with id = " + id;
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable long id) {
        try {
            leaveService.delete(new Leave(id));
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
            List<Leave> retrievedLeaves = leaveService.findByStartDateBetween(inputStartDate, calcEndDate);
             finalListOfLeaves = retrievedLeaves.stream().filter(leave -> (leave.getEmployee().getId() == employeeId)).collect(Collectors.toList());
        } catch (Exception e) {
            return "Error deleting the leave: " + e.toString();
        }
        return "leave successfully with size = " + finalListOfLeaves.size();
    }
}
