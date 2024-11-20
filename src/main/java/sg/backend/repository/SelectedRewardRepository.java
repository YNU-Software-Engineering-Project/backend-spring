package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.SelectedReward;

public interface SelectedRewardRepository extends JpaRepository<SelectedReward,Long> {
}
