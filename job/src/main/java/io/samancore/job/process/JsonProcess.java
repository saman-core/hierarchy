package io.samancore.job.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.samancore.hierarchy.json_diagram.ERDiagram;
import io.samancore.job.entity.EEntity;
import io.samancore.job.entity.REntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class JsonProcess {
    private static final Logger log = Logger.getLogger(JsonProcess.class);

    @ConfigProperty(name = "app.json.path")
    String jsonFilePath;

    @Inject
    private EntityManager entityManager;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    public void run() {
        log.info("INIT load json.");
        try (InputStream inputStream = new FileInputStream(jsonFilePath)) {
            var erDiagram = objectMapper.readValue(inputStream, ERDiagram.class);

            deleteAllRelationships();
            deleteAllEntities();

            Map<String, EEntity> entityMap = new HashMap<>();
            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Rectangle".equals(cell.getType()))
                    .forEach(cell -> {
                        EEntity entity = new EEntity();
                        entity.setId(UUID.fromString(cell.getId()));
                        entity.setName(cell.getName());
                        entity.setComment(cell.getComment());
                        entity.setStateType(cell.getStateType().getValue());
                        persistEntity(entity);
                        entityMap.put(cell.getId(), entity);
                        log.infof("Persist entity %s", entity.getName());
                    });

            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Link".equals(cell.getType()))
                    .forEach(link -> {
                        REntity relationship = new REntity();
                        relationship.setId(UUID.fromString(link.getId()));
                        relationship.setName(link.getName());
                        relationship.setSource(entityMap.get(link.getSource().getId()));
                        relationship.setTarget(entityMap.get(link.getTarget().getId()));
                        relationship.setCardinalitySource(link.getCardinalitySource().getValue());
                        relationship.setCardinalityTarget(link.getCardinalityTarget().getValue());
                        persistRelationship(relationship);
                        log.infof("Persist relationship %s", relationship.getName());
                    });
            log.info("Graph loaded successfully.");
        } catch (Exception e) {
            log.error("Error loading Graph", e);
            throw new IllegalStateException("Error loading Graph");
        }
    }

    public void persistEntity(EEntity eEntity) {
        entityManager.persist(eEntity);
    }

    public void deleteAllEntities() {
        entityManager.createQuery("DELETE FROM EEntity").executeUpdate();
    }

    public void persistRelationship(REntity rEntity) {
        entityManager.persist(rEntity);
    }

    public void deleteAllRelationships() {
        entityManager.createQuery("DELETE FROM REntity").executeUpdate();
    }
}
