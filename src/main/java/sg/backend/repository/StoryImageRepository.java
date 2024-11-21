package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Funding;
import sg.backend.entity.StoryImage;

import java.util.List;

public interface StoryImageRepository extends JpaRepository<StoryImage, Long> {
    List<StoryImage> findAllByFunding(Funding funding);
}
