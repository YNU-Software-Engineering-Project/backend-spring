package sg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sg.backend.entity.Funder;
import sg.backend.entity.Funding;

public interface FunderRepository extends JpaRepository<Funder, Long> {
    @Query("SELECT f.funding FROM Funder f WHERE f.user.userId = :userId ORDER BY f.createdAt DESC")
    Page<Funding> findFundingByUserIdOrderByFunderCreatedAt(@Param("userId") Long userId, PageRequest pageRequest);
}
