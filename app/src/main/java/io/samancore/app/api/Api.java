package io.samancore.app.api;

import io.samancore.app.util.GraphQueries;
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
    @Path("entity/{entityId}")
    @RolesAllowed({"admin"})
    public EntityModel getEntity(@PathParam("entityId") String entityId) {
        return GraphQueries.getVertex(entityId);
    }

    @GET
    @Path("relationship/{relationshipId}")
    @RolesAllowed({"admin"})
    public RelationshipModel getRelationship(@PathParam("relationshipId") String relationshipId) {
        return GraphQueries.getEdge(relationshipId);
    }

    @GET
    @Path("entity/{entityId}/relationships")
    @RolesAllowed({"admin"})
    public List<RelationshipModel> getRelationshipsFromEntity(@PathParam("entityId") String entityId) {
        return GraphQueries.getEdgesFromVertex(entityId);
    }

    @GET
    @Path("relationship/{relationshipId}/target-entity")
    @RolesAllowed({"admin"})
    public EntityModel getTargetEntity(@PathParam("relationshipId") String relationshipId) {
        return GraphQueries.getTargetVertex(relationshipId);
    }

    @GET
    @Path("relationship/{relationshipId}/source-entity")
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
