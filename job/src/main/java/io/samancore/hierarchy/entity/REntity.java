package io.samancore.hierarchy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "relationships")
public class REntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private EEntity source;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private EEntity target;

    @Column(name = "cardinality_source")
    private String cardinalitySource;

    @Column(name = "cardinality_target")
    private String cardinalityTarget;
}
