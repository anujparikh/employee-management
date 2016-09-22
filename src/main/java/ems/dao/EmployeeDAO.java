package ems.dao;

import ems.domain.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Repository
public interface EmployeeDAO extends PagingAndSortingRepository<Employee, Long> {

    ArrayList<Employee> findByRole (String role);

    ArrayList<Employee> findByTeamId (Long teamId);

    ArrayList<Employee> findByManagerId (Long managerId);
}
