package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findByUuid(String uuid);
}
