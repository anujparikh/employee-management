package ems.dao;

import ems.domain.Employee;
import ems.domain.Leave;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Transactional
@Repository
public interface LeaveDAO extends PagingAndSortingRepository<Leave, Long> {

    ArrayList<Leave> findByTeamId(Long teamId);

    Set<Leave> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    ArrayList<Leave> findByEmployee(Employee employee);
}
