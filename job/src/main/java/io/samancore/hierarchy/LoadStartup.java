package io.samancore.hierarchy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import io.samancore.hierarchy.entity.EEntity;
import io.samancore.hierarchy.entity.REntity;
import io.samancore.hierarchy.json_diagram.ERDiagram;
import io.samancore.hierarchy.repository.EntityRepository;
import io.samancore.hierarchy.repository.RelationshipRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Startup
@ApplicationScoped
public class LoadStartup {
    private static final Logger log = Logger.getLogger(LoadStartup.class);

    @Inject
    EntityRepository entityRepository;

    @Inject
    RelationshipRepository relationshipRepository;

    @ConfigProperty(name = "app.json")
    String jsonContent;

    @PostConstruct
    @Transactional
    public void init() throws JsonProcessingException {
        log.info("INIT Graph loaded");
        ObjectMapper objectMapper = new ObjectMapper();
        ERDiagram erDiagram = objectMapper.readValue(jsonContent, ERDiagram.class);

        relationshipRepository.deleteAll();
        entityRepository.deleteAll();

        Map<String, EEntity> entityMap = new HashMap<>();
        erDiagram.getCells().stream()
                .filter(cell -> "standard.Rectangle".equals(cell.getType()))
                .forEach(cell -> {
                    EEntity entity = new EEntity();
                    entity.setId(UUID.fromString(cell.getId()));
                    entity.setName(cell.getName());
                    entity.setComment(cell.getComment());
                    entity.setStateType(cell.getStateType().getValue());
                    entityRepository.persist(entity);
                    entityMap.put(cell.getId(), entity);
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
                    relationshipRepository.persist(relationship);
                });
        log.info("Graph loaded successfully.");
    }
}
