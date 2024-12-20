package sg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.backend.entity.Funding;
import sg.backend.entity.FundingLike;
import sg.backend.entity.User;

import java.util.Optional;

public interface FundingLikeRepository extends JpaRepository<FundingLike, Long> {

    boolean existsByUserAndFunding(User user, Funding funding);

    @Query("SELECT fl.funding FROM FundingLike fl WHERE fl.user.userId = :userId ORDER BY fl.createdAt DESC")
    Page<Funding> findFundingLikedByUserIdOrderByLikeCreatedAt(@Param("userId") Long userId, Pageable pageable);

    void deleteByUserAndFunding(User user, Funding funding);

    Optional<FundingLike> findByUserAndFunding(User user,Funding funding);
}
