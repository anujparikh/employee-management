package ems.dao;

import ems.domain.Leave;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Transactional
@Repository
public interface LeaveDAO extends PagingAndSortingRepository<Leave, Long> {

    Set<Leave> findByTeamId(String teamId);

    Set<Leave> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
