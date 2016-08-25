package org.management.controller;

import org.management.domain.Employee;
import org.management.service.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @RequestMapping("/create")
    @ResponseBody
    public String create (String firstName, String lastName, String email, String teamId, String role, String managerId) {
        String employeeId = "";
        try {
            Employee employee = new Employee(firstName, lastName, email, teamId, role, managerId);
            employeeDao.save(employee);
            employeeId = String.valueOf(employee.getId());
        } catch (Exception e) {
            return "Error creating the user: " + e.toString();
        }
        return "User succesfully created with id = " + employeeId;
    }
}
