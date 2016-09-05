package ems.controller;

import ems.dao.EmployeeDAO;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class EmployeeControllerTest {

    @Test
    public void testEmployeeControllerCreate() {
        EmployeeDAO mockEmployeeDao = mock(EmployeeDAO.class);
        EmployeeController testEmployeeController = new EmployeeController(mockEmployeeDao);
    }
}
