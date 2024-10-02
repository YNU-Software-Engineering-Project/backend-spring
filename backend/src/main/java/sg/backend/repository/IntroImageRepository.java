package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Funding;
import sg.backend.entity.IntroImage;

import java.util.List;

public interface IntroImageRepository extends JpaRepository<IntroImage, Long> {
    IntroImage findByUuid(String uuid);
    List<IntroImage> findAllByFunding(Funding funding);
}
