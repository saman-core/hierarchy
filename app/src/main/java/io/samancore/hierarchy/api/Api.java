package io.samancore.hierarchy.api;

import io.samancore.hierarchy.util.GraphQueries;
import io.samancore.hierarchy.model.EntityModel;
import io.samancore.hierarchy.model.RelationshipModel;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;
import java.util.Set;

@Path("")
public class Api {

    @GET
    @Path("relationships/{entityId}")
    @RolesAllowed({"admin"})
    public List<RelationshipModel> getRelationshipsFromEntity(@PathParam("entityId") String entityId) {
        return GraphQueries.getEdgesFromVertex(entityId);
    }

    @GET
    @Path("target-entity/{relationshipId}")
    @RolesAllowed({"admin"})
    public EntityModel getTargetEntity(@PathParam("relationshipId") String relationshipId) {
        return GraphQueries.getTargetVertex(relationshipId);
    }

    @GET
    @Path("source-entity/{relationshipId}")
    @RolesAllowed({"admin"})
    public EntityModel getSourceEntity(@PathParam("relationshipId") String relationshipId) {
        return GraphQueries.getSourceVertex(relationshipId);
    }

    @GET
    @Path("origin-entities")
    @RolesAllowed({"admin"})
    public Set<EntityModel> getEntitiesNotTarget() {
        return GraphQueries.getVerticesNotTarget();
    }
}
