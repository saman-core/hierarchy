package io.samancore.hierarchy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "entities")
public class EEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String comment;

    @Column(name = "state_type")
    private String stateType;
}
