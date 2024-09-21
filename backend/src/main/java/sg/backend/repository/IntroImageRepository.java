package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.IntroImage;

public interface IntroImageRepository extends JpaRepository<IntroImage, Long> {
    IntroImage findByUuid(String uuid);
}
