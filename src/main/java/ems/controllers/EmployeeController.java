package ems.controllers;

import ems.domain.Employee;
import ems.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EmployeeController implements ErrorController {

    private static final String PATH = "/error";

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/create")
    @ResponseBody
    public String create(String firstName,
                         String lastName,
                         String email,
                         String teamId,
                         String role,
                         @RequestParam(value = "managerId", required = false) String managerId) {
        String employeeId = "";
        Employee employee;
        try {
            employee = new Employee(firstName, lastName, email, teamId, role, managerId);
            employeeService.save(employee);
            employeeId = String.valueOf(employee.getId());
        } catch (Exception e) {
            return "Error creating the user: " + e.toString();
        }
        return "User successfully created with id = " + employeeId;
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(@RequestParam("id") long id,
                         @RequestParam(value = "firstName", required = false) String firstName,
                         @RequestParam(value = "lastName", required = false) String lastName,
                         @RequestParam(value = "email", required = false) String email,
                         @RequestParam(value = "teamId", required = false) String teamId,
                         @RequestParam(value = "role", required = false) String role,
                         @RequestParam(value = "managerId", required = false) String managerId) {
        try {
            Employee employeeToBeUpdated = employeeService.findOne(id);
            if (firstName != null) employeeToBeUpdated.setFirstName(firstName);
            if (lastName != null) employeeToBeUpdated.setLastName(lastName);
            if (email != null) employeeToBeUpdated.setEmail(email);
            if (teamId != null) employeeToBeUpdated.setTeamId(teamId);
            if (role != null) employeeToBeUpdated.setRole(role);
            if (managerId != null) employeeToBeUpdated.setManagerId(managerId);
            employeeService.save(employeeToBeUpdated);
        } catch (Exception e) {
            return "Error updating the user: " + e.toString();
        }
        return "User updated successfully";
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") long id) {
        try {
            employeeService.delete(new Employee(id));
        } catch (Exception e) {
            return "Error deleting the user: " + e.toString();
        }
        return "User " + id + " deleted successfully";
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public String findOneById(@PathVariable("id") long id) {
        Employee retrievedEmployee;
        try {
            retrievedEmployee = employeeService.findOne(id);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Retrieved Employee with employee first name: " + retrievedEmployee.getFirstName();
    }

    @RequestMapping("/team/{teamId}")
    @ResponseBody
    public String findByTeamId(@PathVariable("teamId") String teamId) {
        List<Employee> retrievedEmployeeList;
        try {
            retrievedEmployeeList = employeeService.findByTeamId(teamId);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Total Retrieved Employees for team with id " + teamId + " is " + retrievedEmployeeList.size();
    }

    @RequestMapping("/role/{role}")
    @ResponseBody
    public String findByRole(@PathVariable("role") String role) {
        List<Employee> retrievedEmployeeList;
        try {
            retrievedEmployeeList = employeeService.findByRole(role);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Total Retrieved Employees for role " + role + " is " + retrievedEmployeeList.size();
    }

    @RequestMapping("/manager/{managerId}")
    @ResponseBody
    public String findByManagerId(@PathVariable("managerId") String managerId) {
        List<Employee> retrievedEmployeeList;
        try {
            retrievedEmployeeList = employeeService.findByManagerId(managerId);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Total Retrieved Employees working with manager id " + managerId + " is " + retrievedEmployeeList.size();
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
