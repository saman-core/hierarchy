package io.samancore.hierarchy.repository;

import io.samancore.hierarchy.entity.EEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class EntityRepository {

    @Inject
    private EntityManager entityManager;

    public void persist(EEntity eEntity) {
        entityManager.persist(eEntity);
    }

    public void deleteAll() {
        entityManager.createQuery("DELETE FROM EEntity").executeUpdate();
    }

    public Optional<EEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(EEntity.class, id));
    }
}
