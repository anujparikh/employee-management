package org.management.service;

import org.management.domain.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface EmployeeDao extends CrudRepository<Employee, Long> {

    public List<Employee> findByManagerid (String managerId);

    public List<Employee> findByRole (String role);

    public List<Employee> findByTeamid (String teamId);

}
