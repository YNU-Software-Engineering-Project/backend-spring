package sg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.backend.entity.Funding;
import sg.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByFunding(Funding funding, Pageable pageable);
}

