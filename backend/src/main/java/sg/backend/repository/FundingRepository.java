package sg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Funding;
import sg.backend.entity.User;

import java.util.Optional;

public interface FundingRepository extends JpaRepository<Funding, Long> {

    Page<Funding> findByUser(User user, PageRequest pageRequest);

    Optional<Funding> findByFundingId(Long fundingId);
}
