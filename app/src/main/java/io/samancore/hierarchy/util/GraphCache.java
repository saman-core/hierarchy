package io.samancore.hierarchy.util;

import io.samancore.hierarchy.model.EntityModel;
import io.samancore.hierarchy.model.RelationshipModel;
import org.jgrapht.graph.DefaultDirectedGraph;

public class GraphCache {
    public static final DefaultDirectedGraph<EntityModel, RelationshipModel> GRAPH = new DefaultDirectedGraph<>(RelationshipModel.class);

    private GraphCache() {
    }
}
