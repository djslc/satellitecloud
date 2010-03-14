package uk.me.g4dpz.gae.satellitecloud.server;

import java.util.Date;

public interface Clock {
	long currentTime();

	Date currentDate();
}
