package uk.me.g4dpz.gae.satellitecloud.server.rest;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.me.g4dpz.gae.satellitecloud.server.implementation.PredictionServiceShared;

@Produces("application/xml")
@Component
public class PredictionServiceRest {
	
	@Autowired
	PredictionServiceShared predictionServiceShared;

}
