/**
 * 
 */
package uk.me.g4dpz.gae.satellitecloud.server.implementation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import uk.me.g4dpz.gae.satellitecloud.server.Clock;

/**
 * @author badger
 *
 */
public class UTCClock implements Clock {
	
	private TimeZone tz = new SimpleTimeZone(0, "UTC");
	private Calendar cal;
	
	/**
	 * Constructor.
	 */
	public UTCClock() {
		cal = Calendar.getInstance(tz);
	}

	/* (non-Javadoc)
	 * @see uk.me.g4dpz.gae.satellitecloud.server.Clock#currentDate()
	 */
	@Override
	public Date currentDate() {
		return cal.getTime();
	}

	/* (non-Javadoc)
	 * @see uk.me.g4dpz.gae.satellitecloud.server.Clock#currentTime()
	 */
	@Override
	public long currentTime() {
		return cal.getTimeInMillis();
	}

}
