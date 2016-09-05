package ems.controller;

import ems.dao.EmployeeDAO;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class EmployeeControllerTest {

    @Test
    public void testEmployeeControllerCreate() {
        EmployeeDAO mockEmployeeDAO = mock(EmployeeDAO.class);
        EmployeeController testEmployeeController = new EmployeeController(mockEmployeeDAO);
        Assert.assertEquals("User successfully created with id = 0", testEmployeeController.create("testFirstName", "testLastName", "testEmail", "testTeamId", "testRole", 123L));
    }
}
