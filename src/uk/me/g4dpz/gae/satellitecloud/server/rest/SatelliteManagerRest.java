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

import uk.me.g4dpz.gae.satellitecloud.server.implementation.SatelliteManagerShared;

@Produces("application/xml")
@Component
public class SatelliteManagerRest {
	
	@Autowired
	SatelliteManagerShared satelliteManagerShared;

	public SatelliteManagerRest() {
	}
	
	@GET
    @Path("/keps/{id}")
    public Response loadKeps(
            @Context final Request request,
            @Context final HttpHeaders headers,
            @PathParam("id") final String id) {
		
		return loadKeps(request, headers, id, null);
	}
	
	@GET
    @Path("/keps/{id}/{file}")
    public Response loadKeps(
            @Context final Request request,
            @Context final HttpHeaders headers,
            @PathParam("id") final String id,
            @PathParam("file") final String file) {
		
		final ResponseBuilder responseBuilder = Response.ok(satelliteManagerShared.loadKeps(id, file));
		
		return responseBuilder.build();
	}
	
	@GET
    @Path("/predict/{hours}/{seconds}")
    public Response preLoadSatellitePredictions(
            @Context final Request request,
            @Context final HttpHeaders headers,
            @PathParam("hours") final String hours,
            @PathParam("seconds") final String seconds) {
		
		final ResponseBuilder responseBuilder 
			= Response.ok(satelliteManagerShared.preLoadSatellitePredictions(hours, seconds));
		
		return responseBuilder.build();
	}

}
