package ems.services;

import ems.domain.Leave;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface LeaveService extends CrudRepository<Leave, Long> {

    public List<Leave> findByTeamId(String teamId);
}