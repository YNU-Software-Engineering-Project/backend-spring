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

    private String name;

    private String uuid;

    private String fpath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    public Document( Funding funding, String document_name, String document_uuid, String document_path){
        this.funding = funding;
        this.name = document_name;
        this.uuid = document_uuid;
        this.fpath = document_path;
    }

}
