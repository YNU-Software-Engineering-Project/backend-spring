package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Document;
import sg.backend.entity.Funding;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Document findByUuid(String uuid);
    List<Document> findAllByFunding(Funding funding);
}
