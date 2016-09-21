package ems.controller;

import ems.dao.EmployeeDAO;
import ems.domain.Employee;
import ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Set;

@Controller
@RequestMapping("/employeecontroller")
public class EmployeeController implements ErrorController {

    private static final String PATH = "/error";

    private final EmployeeDAO employeeDAO;
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeDAO employeeDAO, EmployeeService employeeService) {
        this.employeeDAO = employeeDAO;
        this.employeeService = employeeService;
    }

    /**
     * @return - list of all employees
     */
    @RequestMapping(value = "/employee/", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Employee>> listAllEmployees() {
        System.out.println("Inside fetch all Employees");
        ArrayList<Employee> listOfEmployees;
        try {
            listOfEmployees = (ArrayList<Employee>) employeeDAO.findAll();
            System.out.println("List of Employees: " + listOfEmployees);
            listOfEmployees.forEach(i -> {
                System.out.println("Employee id: " + i.getId());
            });
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listOfEmployees, HttpStatus.OK);
    }

    /**
     * @param id - employee id
     * @return - fetched employee based on id
     */
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) {
        System.out.println("Inside fetch by id employee");
        Employee retrievedEmployee;
        try {
            retrievedEmployee = employeeDAO.findOne(id);
            System.out.println("Employee name: " + retrievedEmployee.getFirstName());
        } catch (Exception e) {
            return new ResponseEntity<Employee>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Employee>(retrievedEmployee, HttpStatus.OK);
    }

    /**
     *
     * @param employee
     * @param uriComponentsBuilder
     * @return - new Employee created
     */
    @RequestMapping(value = "/employee/", method = RequestMethod.POST)
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee, UriComponentsBuilder uriComponentsBuilder) {
        System.out.println("Inside create Employee");
        if (employeeService.isEmployeeExist(employee)) {
            return new ResponseEntity<Employee>(HttpStatus.CONFLICT);
        }
        try {
            employeeDAO.save(employee);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/employee/{id}").buildAndExpand(employee.getId()).toUri());
            return new ResponseEntity<Employee>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Employee>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping("/create")
    @ResponseBody
    public String create(String firstName,
                         String lastName,
                         String email,
                         String teamId,
                         String role,
                         @RequestParam(value = "managerId", required = false) Long managerId) {
        String employeeId = "";
        Employee employee;
        try {
            Employee manager = managerId != null ? employeeDAO.findOne(managerId) : null;
            employee = new Employee(firstName, lastName, email, teamId, role, manager);
            employeeDAO.save(employee);
            employeeId = String.valueOf(employee.getId());
        } catch (Exception e) {
            return "Error creating the user: " + e.toString();
        }
        return "User successfully created with id = " + employeeId;
    }

    @RequestMapping("/update/{id}")
    @ResponseBody
    public String update(@PathVariable("id") Long id,
                         @RequestParam(value = "firstName", required = false) String firstName,
                         @RequestParam(value = "lastName", required = false) String lastName,
                         @RequestParam(value = "email", required = false) String email,
                         @RequestParam(value = "teamId", required = false) String teamId,
                         @RequestParam(value = "role", required = false) String role,
                         @RequestParam(value = "managerId", required = false) Long managerId) {
        try {
            Employee employeeToBeUpdated = employeeDAO.findOne(id);
            if (firstName != null) employeeToBeUpdated.setFirstName(firstName);
            if (lastName != null) employeeToBeUpdated.setLastName(lastName);
            if (email != null) employeeToBeUpdated.setEmail(email);
            if (teamId != null) employeeToBeUpdated.setTeamId(teamId);
            if (role != null) employeeToBeUpdated.setRole(role);
            if (managerId != null) employeeToBeUpdated.setManager(employeeDAO.findOne(managerId));
            employeeDAO.save(employeeToBeUpdated);
        } catch (Exception e) {
            return "Error updating the user: " + e.toString();
        }
        return "User updated successfully";
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        try {
            employeeDAO.delete(new Employee(id));
        } catch (Exception e) {
            return "Error deleting the user: " + e.toString();
        }
        return "User " + id + " deleted successfully";
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public String findOneById(@PathVariable("id") Long id) {
        Employee retrievedEmployee;
        try {
            retrievedEmployee = employeeDAO.findOne(id);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Retrieved Employee with employee first name: " + retrievedEmployee.getFirstName();
    }

    @RequestMapping("/{teamId}/team-list")
    @ResponseBody
    public String findByTeamId(@PathVariable String teamId) {
        Set<Employee> retrievedEmployeeList;
        try {
            retrievedEmployeeList = employeeDAO.findByTeamId(teamId);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Total Retrieved Employees for team with id " + teamId + " is " + retrievedEmployeeList.size();
    }

    @RequestMapping("/{role}/role-list")
    @ResponseBody
    public String findByRole(@PathVariable String role) {
        Set<Employee> retrievedEmployeeList;
        try {
            retrievedEmployeeList = employeeDAO.findByRole(role);
        } catch (Exception e) {
            return "Error fetching the employee";
        }
        return "Total Retrieved Employees for role " + role + " is " + retrievedEmployeeList.size();
    }

    @RequestMapping("/{managerId}/employees")
    @ResponseBody
    public String findByManagerId(@PathVariable Long managerId) {
        Set<Employee> retrievedEmployeeList;
        try {
            retrievedEmployeeList = employeeDAO.findByManagerId(managerId);
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
