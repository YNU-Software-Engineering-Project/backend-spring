package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Funding;
import sg.backend.entity.Reward;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward,Long> {
    List<Reward> findAllByFunding(Funding funding);
    List<Reward> findByFunding_Funding_id(Long funding_id);
}
