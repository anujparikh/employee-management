package ems.controller;

import ems.dao.EmployeeDAO;
import ems.dao.LeaveDAO;
import ems.domain.Employee;
import ems.domain.Leave;
import ems.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
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
     * @return list of all leaves
     */
    @RequestMapping(value = "/leave/", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Leave>> listAllLeaves() {
        System.out.println("Inside fetch all leaves");
        ArrayList<Leave> listOfLeaves;
        try {
            listOfLeaves = (ArrayList<Leave>) leaveDAO.findAll();
            System.out.println("List of Leveas: " + listOfLeaves);
            listOfLeaves.forEach(i -> {
                System.out.println("Leave id: " + i.getId());
            });
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listOfLeaves, HttpStatus.OK);
    }

    /**
     * @param startDate  - start date of the range of leaves to be retrieved
     * @param noOfDays   - total no of days to calculate end date of range
     * @param employeeId - employee id for which leaves needs to be fetched
     * @return - Set of retrieved leaves
     */
    @RequestMapping(value = "/leave/range", method = RequestMethod.GET)
    public ResponseEntity<List<Leave>> listAllLeavesWithinRange(String startDate,
                                                                Integer noOfDays,
                                                                Long employeeId) {
        System.out.println("Inside fetch all leaves with range");
        List<Leave> listOfLeaves;
        try {
            LocalDate inputStartDate = LocalDate.parse(startDate, formatter);
            LocalDate calcEndDate = inputStartDate.plusDays(noOfDays);
            listOfLeaves = leaveService.findByStartDateBetweenForEmployeeId(inputStartDate, calcEndDate, employeeId);
            listOfLeaves.forEach(i -> {
                System.out.println("No of days for " + i.getEmployee().getFirstName() + " is: " + i.getNoOfDays());
            });
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listOfLeaves, HttpStatus.OK);
    }

    /**
     * @param leaveId - leave id for which leave needs to be retrieved
     * @return - Leave for particular leave id
     */
    @RequestMapping(value = "/leave/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Leave> getLeave(@PathVariable("id") Long leaveId) {
        System.out.println("Inside fetch leave by id");
        Leave retrievedLeave;
        try {
            retrievedLeave = leaveDAO.findOne(leaveId);
            System.out.println("Name: " + retrievedLeave.getEmployee().getFirstName() + " leave id: " + retrievedLeave.getId());
        } catch (Exception e) {
            return new ResponseEntity<Leave>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<Leave>(retrievedLeave, HttpStatus.OK);
    }

    /**
     * @param leaveId
     * @return - List of leaves for particular team
     */
    @RequestMapping(value = "/leave/team/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<Leave>> getLeaveByTeamId(@PathVariable("id") Long leaveId) {
        System.out.println("Inside fetch leave by id");
        ArrayList<Leave> listOfLeaves;
        try {
            listOfLeaves = leaveDAO.findByTeamId(leaveId);
            listOfLeaves.forEach(i -> {
                System.out.println("No of days for " + i.getEmployee().getFirstName() + " is: " + i.getNoOfDays());
            });
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listOfLeaves, HttpStatus.OK);
    }

    /**
     *
     * @param id - employee id
     * @return - list of leaves for that employee id
     */
    @RequestMapping(value = "/leave/employee/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<Leave>> getLeaveByEmployeeId(@PathVariable("id") Long id) {
        System.out.println("Inside find leaves by employee id");
        ArrayList<Leave> listOfLeaves;
        try {
            listOfLeaves = leaveDAO.findByEmployee(employeeDAO.findOne(id));
            listOfLeaves.forEach(i -> {
                System.out.println("Leave id: " + i.getId());
            });
        } catch (Exception e) {
            return new ResponseEntity<ArrayList<Leave>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<ArrayList<Leave>>(listOfLeaves, HttpStatus.OK);
    }

    /**
     * @param leave
     * @param uriComponentsBuilder
     * @return - new leave created
     */
    @RequestMapping(value = "/leave/", method = RequestMethod.POST)
    public ResponseEntity<Leave> createLeave(@RequestBody Leave leave, UriComponentsBuilder uriComponentsBuilder) {
        System.out.println("Inside create Leave");
        if (leaveService.isLeaveExist(leave)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try {
            leaveDAO.save(leave);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriComponentsBuilder.path("/leave/{id}").buildAndExpand(leave.getId()).toUri());
            return new ResponseEntity<>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * @param id
     * @param leave
     * @return - updated leave
     */
    @RequestMapping(value = "/leave/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Leave> update(@PathVariable Long id, @RequestBody Leave leave) {
        System.out.println("Inside update leave");
        try {
            leave = leaveService.updateLeave(leave);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(leave, HttpStatus.OK);
    }

    /**
     * @param id - leave id for leave to be deleted
     * @return - Boolean - successful or failure
     */
    @RequestMapping(value = "/leave/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Leave> delete(@PathVariable("id") Long id) {
        System.out.println("Inside delete leave");
        Leave retrievedLeaveToBeDeleted = leaveDAO.findOne(id);

        if (retrievedLeaveToBeDeleted == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            //leaveDAO.delete(new Leave(id)); // TODO: need to add a method in service for deleting leave
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * @param id
     * @return - list of approvers for leave id
     */
    @RequestMapping(value = "/leave/approverlist/{id}", method = RequestMethod.GET)
    public ResponseEntity<Set<Employee>> findApproverListByLeaveId(@PathVariable("id") Long id) {
        System.out.println("Inside approver list by leave Id");
        Set<Employee> listOfEmployees;
        try {
            listOfEmployees = leaveService.findApproverSetByLeaveId(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(listOfEmployees, HttpStatus.OK);
    }

    @RequestMapping(value = "/leave/assigned/{id}")
    public ResponseEntity<List<Leave>> findByApproverEmployeeId(@PathVariable("id") Long id) {
        List<Leave> listOfLeaves;

        try {
            listOfLeaves = leaveService.findLeaveSetByEmployeeId(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(listOfLeaves, HttpStatus.OK);
    }

    //TODO: Implement DeleteAll Method in controller
}
