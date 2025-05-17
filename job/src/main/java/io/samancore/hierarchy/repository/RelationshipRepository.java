package io.samancore.hierarchy.repository;

import io.samancore.hierarchy.entity.REntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RelationshipRepository {

    @Inject
    private EntityManager entityManager;

    public void persist(REntity rEntity) {
        entityManager.persist(rEntity);
    }

    public void deleteAll() {
        entityManager.createQuery("DELETE FROM REntity").executeUpdate();
    }

    public Optional<REntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(REntity.class, id));
    }
}
