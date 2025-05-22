package io.samancore.app.util;

import io.samancore.hierarchy.model.EntityModel;
import io.samancore.hierarchy.model.RelationshipModel;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.samancore.app.util.GraphCache.GRAPH;

public class GraphQueries {

    private GraphQueries() {
    }

    public static EntityModel getVertex(String vertexId) {
        return GRAPH.vertexSet().stream()
                .filter(vertex -> vertex.id().equals(vertexId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Vertex not found"));
    }

    public static RelationshipModel getEdge(String edgeId) {
        return GRAPH.edgeSet().stream()
                .filter(edge -> edge.id().equals(edgeId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Edge not found"));
    }

    public static List<RelationshipModel> getEdgesFromVertex(String vertexId) {
        EntityModel entityModel = new EntityModel(vertexId, null, null, null);
        return GRAPH.outgoingEdgesOf(entityModel).stream().toList();
    }

    public static EntityModel getTargetVertex(String edgeId) {
        RelationshipModel relationshipModel = new RelationshipModel(edgeId, null, null, null, null, null);
        return GRAPH.getEdgeTarget(relationshipModel);
    }

    public static EntityModel getSourceVertex(String edgeId) {
        RelationshipModel relationshipModel = new RelationshipModel(edgeId, null, null, null, null, null);
        return GRAPH.getEdgeSource(relationshipModel);
    }

    public static Set<EntityModel> getVerticesNotTarget() {
        Set<EntityModel> targetVertices = GRAPH.edgeSet().stream()
                .map(GRAPH::getEdgeTarget)
                .collect(Collectors.toSet());
        return GRAPH.vertexSet().stream()
                .filter(vertex -> !targetVertices.contains(vertex))
                .collect(Collectors.toSet());
    }
}
