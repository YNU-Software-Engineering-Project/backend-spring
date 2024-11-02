package sg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.backend.entity.Funding;
import sg.backend.entity.State;

import java.util.List;
import sg.backend.entity.User;

public interface FundingRepository extends JpaRepository<Funding, Long> {

    List<Funding> findTop3ByCurrentOrderByCreatedAtDesc(State current);

    List<Funding> findTop3ByCurrentOrderByTotalLikesDesc(State current);

    List<Funding> findTop3ByCurrentOrderByTargetAmountAsc(State current);

    @Query("SELECT f FROM Funding f WHERE f.current = :current ORDER BY (f.currentAmount / f.targetAmount) DESC")
    List<Funding> findTop3ByCurrentOrderByAchievementRateDesc(@Param("current") State current);

    Page<Funding> findByUser(User user, PageRequest pageRequest);

    long countByCurrent(State state);

    List<Funding> findByCurrent(State state);

    @Query("SELECT COUNT(f) FROM Funding f WHERE f.funding_id = :fundingId")
    long countFundersByFundingId(@Param("fundingId") Long fundingId);
}