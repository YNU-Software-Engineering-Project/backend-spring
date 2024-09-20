package sg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.backend.entity.Funding;
import sg.backend.entity.FundingLike;
import sg.backend.entity.User;

public interface FundingLikeRepository extends JpaRepository<FundingLike, Long> {

    boolean existsByUserAndFunding(User user, Funding funding);

    @Query("SELECT fl.funding FROM FundingLike fl WHERE fl.user.userId = :userId")
    Page<Funding> findFundingLikedByUserId(@Param("userId") Long userId, Pageable pageable);
}
