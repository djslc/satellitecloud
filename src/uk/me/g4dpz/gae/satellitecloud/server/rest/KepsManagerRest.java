package uk.me.g4dpz.gae.satellitecloud.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.me.g4dpz.gae.satellitecloud.server.implementation.KepsManagerShared;

@Produces("application/xml")
@Component
public class KepsManagerRest {
	
	@Autowired
	KepsManagerShared kepsManagerShared;

	public KepsManagerRest() {
	}
	
	@GET
    @Path("/{id}")
    public Response loadKeps(
            @Context final Request request,
            @Context final HttpHeaders headers,
            @PathParam("id") final String id) {
		
		return loadKeps(request, headers, id, null);
	}
	
	@GET
    @Path("/{id}/{file}")
    public Response loadKeps(
            @Context final Request request,
            @Context final HttpHeaders headers,
            @PathParam("id") final String id,
            @PathParam("file") final String file) {
		
		final ResponseBuilder responseBuilder = Response.ok(kepsManagerShared.loadKeps(id, file));
		
		return responseBuilder.build();
	}

}
