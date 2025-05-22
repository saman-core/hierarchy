package io.samancore.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import io.samancore.hierarchy.json_diagram.ERDiagram;
import io.samancore.hierarchy.model.EntityModel;
import io.samancore.hierarchy.model.RelationshipModel;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static io.samancore.app.util.GraphCache.GRAPH;

@Startup
@ApplicationScoped
public class LoadStartup {
    private static final Logger log = Logger.getLogger(LoadStartup.class);

    @ConfigProperty(name = "app.json.path")
    String jsonFilePath;

    @PostConstruct
    public void init() {
        log.info("INIT Graph loaded");

        try (InputStream inputStream = new FileInputStream(jsonFilePath)) {
            var objectMapper = new ObjectMapper();
            var erDiagram = objectMapper.readValue(inputStream, ERDiagram.class);

            Map<String, EntityModel> entityMap = new HashMap<>();
            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Rectangle".equals(cell.getType()))
                    .forEach(cell -> {
                        var entityModel = new EntityModel(
                                cell.getId(),
                                cell.getName(),
                                cell.getComment(),
                                cell.getStateType()
                        );
                        entityMap.put(cell.getId(), entityModel);
                        GRAPH.addVertex(entityModel);
                        log.infof("Vertex: %s", entityModel.name());
                    });

            erDiagram.getCells().stream()
                    .filter(cell -> "standard.Link".equals(cell.getType()))
                    .forEach(link -> {
                        EntityModel source = entityMap.get(link.getSource().getId());
                        EntityModel target = entityMap.get(link.getTarget().getId());

                        if (source != null && target != null) {
                            var relationship = new RelationshipModel(
                                    link.getId(),
                                    link.getName(),
                                    source,
                                    target,
                                    link.getCardinalitySource(),
                                    link.getCardinalityTarget()
                            );
                            GRAPH.addEdge(source, target, relationship);
                            log.infof("Edge: %s", relationship.name());
                        }
                    });

            log.info("Graph loaded successfully.");
        } catch (Exception e) {
            log.error("Error loading Graph", e);
        }
    }
}
