package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.backend.entity.FundingTag;
import sg.backend.entity.Tag;

import java.util.List;

public interface FundingTagRepository extends JpaRepository<FundingTag, Long> {

    @Query("SELECT ft.tag FROM FundingTag ft WHERE ft.funding.fundingId = :fundingId")
    List<Tag> findTagByFundingId(@Param("fundingId") Long fundingId);
}
