package com.elreinodelolvido.ellibertad.cache;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/api/cache")
@Produces(MediaType.APPLICATION_JSON)
public class AnalisisCacheHealthController {

    @Inject
    private AnalisisCacheManager cacheManager;

    public AnalisisCacheHealthController() {}

    @GET
    @Path("/health")
    public Response health() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("lastUpdated", cacheManager.getLastUpdated());
        payload.put("stale", cacheManager.isStale());
        return Response.ok(payload).build();
    }
}