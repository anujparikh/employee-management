package ems.controllers;

import ems.domain.Employee;
import ems.services.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmployeeController implements ErrorController {

    private static final String PATH = "/error";

    @Autowired
    private EmployeeDao employeeDao;

    @RequestMapping("/create")
    @ResponseBody
    public String create(String firstName, String lastName, String email, String teamId, String role, String managerId) {
        System.out.println("inside");
        String employeeId = "";
        Employee employee;
        try {
            employee = new Employee(firstName, lastName, email, teamId, role, managerId);
            employeeDao.save(employee);
            employeeId = String.valueOf(employee.getId());
        } catch (Exception e) {
            return "Error creating the user: " + e.toString();
        }
        return "User succesfully created with id = " + employeeId;
    }

    @RequestMapping(value = PATH)
    public String error() {
        return "Error handling";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
