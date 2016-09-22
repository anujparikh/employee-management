package ems.controller;

import ems.dao.EmployeeDAO;
import ems.domain.Employee;
import ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;

@RestController
public class EmployeeController {

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

    /**
     * @param id
     * @param employee
     * @return - updated Employee
     */
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Employee> update(@PathVariable("id") Long id, @RequestBody Employee employee) {
        System.out.println("Inside Employee Update");
        Employee currentEmployeeToBeUpdated = employeeDAO.findOne(id);

        if (currentEmployeeToBeUpdated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            currentEmployeeToBeUpdated.setFirstName(employee.getFirstName());
            currentEmployeeToBeUpdated.setLastName(employee.getLastName());
            currentEmployeeToBeUpdated.setEmail(employee.getEmail());
            currentEmployeeToBeUpdated.setTeamId(employee.getTeamId());
            currentEmployeeToBeUpdated.setRole(employee.getRole());
            currentEmployeeToBeUpdated.setManager(employee.getManager());
            employeeDAO.save(currentEmployeeToBeUpdated);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(currentEmployeeToBeUpdated, HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Employee> delete(@PathVariable("id") Long id) {
        System.out.println("Inside delete employee");
        Employee retrievedEmployeeToBeDeleted = employeeDAO.findOne(id);

        if (retrievedEmployeeToBeDeleted == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            //employee.delete(new Employee(id)); // TODO: need to add a method in service for deleting employee
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * @param id - team id
     * @return - returns list of employees for that team
     */
    @RequestMapping(value = "/employee/team/id", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Employee>> findByTeamId(@PathVariable("id") Long id) {
        ArrayList<Employee> retrievedEmployees;
        try {
            retrievedEmployees = employeeDAO.findByTeamId(id);

            if (retrievedEmployees.isEmpty()) {
                return new ResponseEntity<ArrayList<Employee>>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<ArrayList<Employee>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<ArrayList<Employee>>(retrievedEmployees, HttpStatus.OK);
    }

    /**
     * @param role - role for which employees needs to be fetched
     * @return - List of employees for that role
     */
    @RequestMapping(value = "/employee/role/{role}", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Employee>> findByRole(@PathVariable("role") String role) {
        ArrayList<Employee> retrievedEmployees;
        try {
            retrievedEmployees = employeeDAO.findByRole(role);

            if (retrievedEmployees.isEmpty()) {
                return new ResponseEntity<ArrayList<Employee>>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<ArrayList<Employee>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<ArrayList<Employee>>(retrievedEmployees, HttpStatus.OK);
    }

    /**
     * @param id - manager employee id
     * @return - list of employees having
     */
    @RequestMapping(value = "/employee/manager/id", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Employee>> findByManagerId(@PathVariable("id") Long id) {
        ArrayList<Employee> retrievedEmployees;
        try {
            retrievedEmployees = employeeDAO.findByManagerId(id);

            if (retrievedEmployees.isEmpty()) {
                return new ResponseEntity<ArrayList<Employee>>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<ArrayList<Employee>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<ArrayList<Employee>>(retrievedEmployees, HttpStatus.OK);
    }
}
