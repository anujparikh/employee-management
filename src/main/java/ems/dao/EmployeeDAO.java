package ems.dao;

import ems.domain.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Repository
public interface EmployeeDAO extends PagingAndSortingRepository<Employee, Long> {

    Set<Employee> findByRole (String role);

    Set<Employee> findByTeamId (String teamId);

    Set<Employee> findByManagerId (long managerId);
}
