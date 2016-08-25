package ems.services;

import ems.domain.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface EmployeeService extends CrudRepository<Employee, Long> {

    public List<Employee> findByRole (String role);

    public List<Employee> findByTeamId (String teamId);

    public List<Employee> findByManagerId (String managerId);
}
