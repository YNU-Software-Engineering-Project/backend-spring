package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Funding;
import sg.backend.entity.FundingTag;

import java.util.List;

public interface FundingTagRepository extends JpaRepository<FundingTag, Long> {
    List<FundingTag> findAllByFunding(Funding funding);
}
