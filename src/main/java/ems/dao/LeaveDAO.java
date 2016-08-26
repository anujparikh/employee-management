package ems.dao;

import ems.domain.Leave;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public interface LeaveDAO extends PagingAndSortingRepository<Leave, Long> {

    List<Leave> findByTeamId(String teamId);

    List<Leave> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
