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
import java.util.Optional;

public interface FundingRepository extends JpaRepository<Funding, Long> {

    Page<Funding> findByUserUserId(Long userId, PageRequest pageRequest);

    Page<Funding> findAllByCurrentOrderByCreatedAtDesc(State current, Pageable pageable);

    List<Funding> findTop3ByCurrentOrderByTotalLikesDesc(State current);

    Page<Funding> findAllByCurrentOrderByTargetAmountAsc(State current, Pageable pageable);

    @Query("SELECT f FROM Funding f WHERE f.current = :current ORDER BY (f.currentAmount / f.targetAmount) DESC")
    Page<Funding> findAllByCurrentOrderByAchievementRateDesc(@Param("current") State current, Pageable pageable);

    Page<Funding> findByUser(User user, PageRequest pageRequest);

    Optional<Funding> findByFundingId(Long fundingId);
}
