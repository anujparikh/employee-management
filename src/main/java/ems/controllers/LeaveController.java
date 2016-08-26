package ems.controllers;

import ems.domain.Employee;
import ems.domain.Leave;
import ems.services.EmployeeService;
import ems.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.Date;

@Controller
@RequestMapping("/leavecontroller")
public class LeaveController {

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
            Date inputStartDate = Date.from(Instant.now());
            System.out.println("Input Start Date: " + inputStartDate);
            Employee temp = employeeService.findOne(employeeId);
            System.out.println("Employee: " + temp.toString());
            leave = new Leave(inputStartDate, 2, temp, "N");
            System.out.println("Leave: " + leave);
            leaveService.save(leave);
            leaveId = String.valueOf(leave.getId());
        } catch (Exception e) {
            return "Error creating the leave: " + e.toString();
        }
        return "Leave successfully created with id = " + leaveId;
    }
}
