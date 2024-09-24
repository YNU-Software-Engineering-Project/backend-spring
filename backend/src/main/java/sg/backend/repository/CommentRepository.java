package sg.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.backend.entity.Question;
import sg.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByQuestion(Question question, Pageable pageable);
}

