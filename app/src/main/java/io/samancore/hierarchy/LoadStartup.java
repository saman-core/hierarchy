package io.samancore.hierarchy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import io.samancore.hierarchy.json_diagram.ERDiagram;
import io.samancore.hierarchy.model.EntityModel;
import io.samancore.hierarchy.model.RelationshipModel;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.samancore.hierarchy.util.GraphCache.GRAPH;

@Startup
@ApplicationScoped
public class LoadStartup {
    private static final Logger log = Logger.getLogger(LoadStartup.class);

    @ConfigProperty(name = "app.json")
    String jsonContent;

    @PostConstruct
    public void init() throws JsonProcessingException  {
        log.info("INIT Graph loaded");

        var objectMapper = new ObjectMapper();
        var erDiagram = objectMapper.readValue(jsonContent, ERDiagram.class);

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
                });

        erDiagram.getCells().stream()
                .filter(cell -> "standard.Link".equals(cell.getType()))
                .forEach(link -> {
                    EntityModel source = entityMap.get(link.getId());
                    EntityModel target = entityMap.get(link.getId());

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
                    }
                });

        GRAPH.vertexSet().forEach(v -> log.info("Vertex: " + v.name()));
        GRAPH.edgeSet().forEach(e -> log.info("Edge: " + e));
        log.info("Graph loaded successfully.");
    }
}
