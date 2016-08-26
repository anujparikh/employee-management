package ems.dao;

import ems.domain.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface EmployeeDAO extends PagingAndSortingRepository<Employee, Long> {

    public List<Employee> findByRole (String role);

    public List<Employee> findByTeamId (String teamId);

    public List<Employee> findByManagerId (long managerId);
}
