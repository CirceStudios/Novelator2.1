package com.elreinodelolvido.ellibertad.cache;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST endpoint to trigger cache refresh manually.
 * Uses JAX-RS annotations; adapt to your framework if you use Spring or other.
 */
@Path("/api/cache")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalisisCacheController {

    @Inject
    private AnalisisCacheService cacheService;

    public AnalisisCacheController() {}

    /**
     * POST /api/cache/refresh?projectPath=...
     * Returns 200 if refreshed with data, 202 if refresh completed but no data obtained, 400 if projectPath missing.
     */
    @POST
    @Path("/refresh")
    public Response refreshCache(@QueryParam("projectPath") String projectPath) {
        if (projectPath == null || projectPath.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"projectPath is required\"}")
                    .build();
        }
        boolean ok = cacheService.refreshCache(projectPath);
        if (ok) {
            return Response.ok("{\"status\":\"refreshed\"}").build();
        } else {
            return Response.status(Response.Status.ACCEPTED)
                    .entity("{\"status\":\"no-data\"}")
                    .build();
        }
    }
}