package ems.service;

import ems.dao.EmployeeDAO;
import ems.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    public Boolean isEmployeeExist(Employee employee) {
        return employeeDAO.findOne(employee.getId()) != null;
    }
}
