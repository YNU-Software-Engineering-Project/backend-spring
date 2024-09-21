package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Funding;

public interface FundingRepository extends JpaRepository<Funding, Long> {
}
