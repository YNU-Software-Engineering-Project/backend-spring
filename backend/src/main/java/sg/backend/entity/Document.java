package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "approval_document")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long document_id;

    private String document_path;

    @ManyToOne
    @JoinColumn(name = "funding_id")
    private Funding funding;

}
