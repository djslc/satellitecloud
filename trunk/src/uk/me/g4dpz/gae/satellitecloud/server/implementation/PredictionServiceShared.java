package uk.me.g4dpz.gae.satellitecloud.server.implementation;

import org.springframework.beans.factory.annotation.Autowired;

import uk.me.g4dpz.gae.satellitecloud.persistence.Cache;
import uk.me.g4dpz.gae.satellitecloud.persistence.SatelliteElementSet;
import uk.me.g4dpz.satellite.Satellite;
import uk.me.g4dpz.satellite.SatelliteFactory;
import uk.me.g4dpz.satellite.TLE;

public class PredictionServiceShared {
	private static final int MAX_CACHE_SIZE = 100;
	private static Cache<Long, Satellite> cache;
	
	static {
		cache = new Cache<Long, Satellite>(new UTCClock(), MAX_CACHE_SIZE);
	}
	
	@Autowired
	KepsManagerShared kepsManagerShared;

	public Object getPasses(String satellliteId, String groundStationId) {
		Long catNum = Long.parseLong(satellliteId);
		Satellite sat = SatelliteFactory.createSatellite(kepsManagerShared.getTLE(catNum));
		
		return null;
	}

	public Object getTracking(String satellliteId, String groundStationId,
			String msTimeUTC) {
		Long catNum = Long.parseLong(satellliteId);
		Satellite sat = SatelliteFactory.createSatellite(kepsManagerShared.getTLE(catNum));
		
		return null;
	}

}
