package uk.me.g4dpz.satellitecloud.model;

/*
 WebSat: Satellite orbit prediction web service

 Copyright (C)  2004-2009  David A. B. Johnson, G4DPZ.

 Dr. T.S. Kelso is the author of the SGP4/SDP4 orbital models,
 originally written in Fortran and Pascal, and released into
 the public domain through his website (http://www.celestrak.com/).
 Neoklis Kyriazis, 5B4AZ, later re-wrote Dr. Kelso's code in C,
 and released it under the GNU GPL in 2002.
 PREDICT's core is based on 5B4AZ's code translation efforts.

 Author: David A. B. Johnson, G4DPZ <dave@g4dpz.me.uk>

 Comments, questions and bugreports should be submitted via
 http://sourceforge.net/projects/websat/
 More details can be found at the project home page:

 http://websat.sourceforge.net

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, visit http://www.fsf.org/
 */

import java.util.Date;
import java.util.GregorianCalendar;

abstract class Satellite implements ISatellite {

	protected static final double DEG2RAD = 1.745329251994330E-2;
	protected static final double KM2MI = 0.621371;
	protected static final double PI_OVER_TWO = Math.PI / 2.0;
	protected static final double THREE_PI_OVER_TWO = 3.0 * Math.PI / 2.0;
	protected static final double TWO_PI = Math.PI * 2.0;
	protected static final double EPSILON = 1.0E-6;
	protected static final double TWO_THIRDS = 2.0 / 3.0;
	/** J2 Harmonic (WGS '72). */
	protected static final double J2_HARMONIC = 1.0826158E-3;
	/** J3 Harmonic (WGS '72). */
	protected static final double J3_HARMONIC = -2.53881E-6;
	/** J4 Harmonic (WGS '72). */
	protected static final double J4_HARMONIC = -1.65597E-6;
	protected static final double XKE = 7.43669161E-2;
	protected static final double EARTH_RADIUS_KM = 6.378137E3;
	protected static final double MINS_PER_DAY = 1.44E3;
	protected static final double AE = 1.0;
	protected static final double CK2 = 5.413079E-4;
	protected static final double CK4 = 6.209887E-7;
	protected static final double FLATTENING_FACTOR = 3.35281066474748E-3;
	protected static final double EARTH_GRAVITATIONAL_CONSTANT = 3.986008E5;
	protected static final double S = 1.012229;
	protected static final double QOMS2T = 1.880279E-09;
	protected static final double SECS_PER_DAY = 8.6400E4;
	protected static final double EARTH_ROTATIONS_PER_SIDERIAL_DAY = 1.00273790934;
	protected static final double EARTH_ROTATIONS_RADIANS_PER_SIDERIAL_DAY 
		= EARTH_ROTATIONS_PER_SIDERIAL_DAY * TWO_PI;
	protected static final double RHO = 1.5696615E-1;
	protected static final double MFACTOR = 7.292115E-5;
	protected static final double SOLAR_RADIUS_KM = 6.96000E5;
	protected static final double ASTRONOMICAL_UNIT = 1.49597870691E8;

	protected static final double SPEED_OF_LIGHT = 2.99792458E8;

	protected static final double PERIGEE_156_KM = 156.0;
	
	private double s4;
	private double qoms24;
	private double perigee;

	private TLE tle;

	public Satellite(final TLE tle) {
		this.tle = tle;
	}

	protected void calculateSGP4(final double tsince, final Vector4 position,
			final Vector4 velocity, final SatPos satPos) {
	}

	protected void calculateSDP4(final double tsince, final Vector4 position,
			final Vector4 velocity, final SatPos satPos) {
	}

	public synchronized TLE getTLE() {
		return tle;
	}

	/**
	 * Calculates the Julian Day of the Year.
	 * 
	 * The function Julian_Date_of_Year calculates the Julian Date
	 * of Day 0.0 of {year}. This function is used to calculate the
	 * Julian Date of any date by using Julian_Date_of_Year, DOY,
	 * and Fraction_of_Day.
	 *
	 * Astronomical Formulae for Calculators, Jean Meeus,
	 * pages 23-25. Calculate Julian Date of 0.0 Jan aYear
	 * 
	 * @param theYear the year
	 * @return the Julian day number
	 */
	protected static synchronized double julianDateOfYear(final double theYear) {

		long a;
		long b;
		long i;
		double jdoy;
		double aYear;

		aYear = theYear - 1;
		i = (long)Math.floor(aYear / 100);
		a = i;
		i = a / 4;
		b = 2 - a + i;
		i = (long)Math.floor(365.25 * aYear);
		i += 30.6001 * 14;
		jdoy = i + 1720994.5 + b;

		return jdoy;
	}

	/**
	 *  The function Julian_Date_of_Epoch returns the Julian Date of
	 * an epoch specified in the format used in the NORAD two-line
	 * element sets. It has been modified to support dates beyond
	 * the year 1999 assuming that two-digit years in the range 00-56
	 * correspond to 2000-2056. Until the two-line element set format
	 * is changed, it is only valid for dates through 2056 December 31.
	 * 
	 * @param epoch the Epoch
	 * @return The Julian date of the Epoch
	 */
	private static synchronized double juliandDateOfEpoch(final double epoch) {

		double year;
		double day;

		/* Modification to support Y2K */
		/* Valid 1957 through 2056 */
		year = Math.floor(epoch * 1E-3);
		day = (epoch * 1E-3 - year) * 1000.0;

		if (year < 57) {
			year = year + 2000;
		}
		else {
			year = year + 1900;
		}

		return julianDateOfYear(year) + day;
	}

	/**
	 * Read the system clock and return the number of days since 31Dec79
	 * 00:00:00 UTC (daynum 0).
	 * 
	 * @param date the date we wan to get the offset for
	 * @return the number of days offset
	 */
	private static synchronized double calcCurrentDaynum(final Date date) {

		final long now = date.getTime();

		final long then = new GregorianCalendar(1979, 11, 31, 0, 0, 0)
				.getTimeInMillis();
		final long millis = now - then;
		final double days = (double)millis / 1000 / 60 / 60 / 24;
		return days;
	}

	/** 
	 * Returns the square of a double.
	 * 
	 * @param arg the value for which to get the double
	 * @return the arg squared
	 */
	private static synchronized double sqr(final double arg) {
		
		return arg * arg;
	}

	/** 
	 * Calculates scalar magnitude of a vector4 argument.
	 * 
	 * @param v the vector were measuring
	 * 
	 */
	protected static synchronized void magnitude(final Vector4 v) {
		v.setW(Math.sqrt(sqr(v.getX()) + sqr(v.getY()) + sqr(v.getZ())));
	}

	/**
	 * Multiplies the vector v1 by the scalar k.
	 * @param k the multiplier
	 * @param v the vector
	 */
	private synchronized void scaleVector(final double k, final Vector4 v) {
		final double x = v.getX();
		final double y = v.getY();
		final double z = v.getZ();
		v.setX(x * k);
		v.setY(y * k);
		v.setZ(z * k);
		magnitude(v);
	}

	/**
	 * Gets the modulus of a double value.
	 * @param arg1 the value to be tested
	 * @param arg2 the divisor
	 * @return the remainder
	 */
	protected static synchronized double modulus(final double arg1, final double arg2) {
		/* Returns arg1 mod arg2 */

		int i;
		double returnValue;

		returnValue = arg1;
		i = (int)Math.floor(returnValue / arg2);
		returnValue -= i * arg2;

		if (returnValue < 0.0) {
			returnValue += arg2;
		}

		return returnValue;
	}

	private static double frac(final double arg) {
		/* Returns fractional part of double argument */
		return (arg - Math.floor(arg));
	}

	private static double thetaGJD(final double theJD) {
		/* Reference: The 1992 Astronomical Almanac, page B6. */

		double ut;
		double tu;
		double gmst;

		ut = frac(theJD + 0.5);
		final double aJD = theJD - ut;
		tu = (aJD - 2451545.0) / 36525.0;
		gmst = 24110.54841 + tu
				* (8640184.812866 + tu * (0.093104 - tu * 6.2E-6));
		gmst = modulus(gmst + SECS_PER_DAY * EARTH_ROTATIONS_PER_SIDERIAL_DAY * ut, SECS_PER_DAY);

		return TWO_PI * gmst / SECS_PER_DAY;
	}


	/**
	 * Calculates the dot product of two vectors.
	 * @param v1 vector 1
	 * @param v2 vector 2
	 * @return the dot product
	 */
	private static double dot(final Vector4 v1, final Vector4 v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ()
				* v2.getZ();
	}

	/**
	 * Calcultes the mudulus of 2 * PI.
	 * @param testValue the value under test
	 * @return the modulus
	 */
	protected static double mod2PI(final double testValue) {
		/* Returns mod 2PI of argument */

		int i;
		double retVal;

		retVal = testValue;
		i = (int)(retVal / TWO_PI);
		retVal -= i * TWO_PI;

		if (retVal < 0.0) {
			retVal += TWO_PI;
		}

		return retVal;
	}

	/** 
	 * Calculate the geodetic
	 * position of an object given its ECI position pos and time.
	 * It is intended to be used to determine the ground track of
	 * a satellite. The calculations assume the earth to be an
	 * oblate spheroid as defined in WGS '72.
	 *
	 * Reference: The 1992 Astronomical Almanac, page K12.
	 * 
	 * @param time the time
	 * @param position the position
	 * @param satPos the satellite position
	 */
	private static synchronized void calculateLatLonAlt(final double time,
			final Vector4 position, final SatPos satPos) {
		

		double r;
		double e2;
		double phi;
		double c;

		satPos.setTheta(Math.atan2(position.getY(), position.getX()));
		satPos.setLongitude(mod2PI(satPos.getTheta() - thetaGJD(time)));
		r = Math.sqrt(sqr(position.getX()) + sqr(position.getY()));
		e2 = FLATTENING_FACTOR * (2 - FLATTENING_FACTOR);
		satPos.setLatitude(Math.atan2(position.getZ(), r));

		do {
			phi = satPos.getLatitude();
			c = 1 / Math.sqrt(1 - e2 * sqr(Math.sin(phi)));
			satPos.setLatitude(Math.atan2(position.getZ() + EARTH_RADIUS_KM * c * e2
					* Math.sin(phi), r));

		} while (Math.abs(satPos.getLatitude() - phi) >= 1E-10);

		satPos.setAltitude(r / Math.cos(satPos.getLatitude()) - EARTH_RADIUS_KM * c);

		double temp = satPos.getLatitude();

		if (temp > PI_OVER_TWO) {
			temp -= TWO_PI;
			satPos.setLatitude(temp);
		}
	}

	/**
	 * Converts the satellite'S position and velocity
	 * vectors from normalized values to km and km/sec.
	 * 
	 * @param pos the position
	 * @param vel the velocity
	 */
	private synchronized void convertSatState(final Vector4 pos, final Vector4 vel) {
		/* Converts the satellite'S position and velocity */
		/* vectors from normalized values to km and km/sec */
		scaleVector(EARTH_RADIUS_KM, pos);
		scaleVector(EARTH_RADIUS_KM * MINS_PER_DAY / SECS_PER_DAY, vel);
	}

	/**
	 * Get the position of the satellite.
	 * 
	 * @param gsPos the ground station position
	 * @param satPos the position of the satellite
	 * @param date the date
	 */
    public synchronized void getPosition(final GroundStationPosition gsPos,
			final SatPos satPos, final Date date) {

		final Vector4 position = new Vector4();
		final Vector4 velocity = new Vector4();

		/* This is the stuff we need to do repetitively while tracking. */

		final double julUTC = calcCurrentDaynum(date) + 2444238.5;

		/* Convert satellite'S epoch time to Julian */
		/* and calculate time since epoch in minutes */

		final double julEpoch = juliandDateOfEpoch(tle.getEpoch());

		final double tsince = (julUTC - julEpoch) * MINS_PER_DAY;

		if (tle.isDeepspace()) {
			calculateSDP4(tsince, position, velocity, satPos);
		} 
		else {
			calculateSGP4(tsince, position, velocity, satPos);
		}

		/* Scale position and velocity vectors to km and km/sec */
		convertSatState(position, velocity);

		/* Calculate velocity of satellite */

		magnitude(velocity);

		final Vector4 squintVector = new Vector4();

		//
		// /** All angles in rads. Distance in km. Velocity in km/S **/
		// /* Calculate satellite Azi, Ele, Range and Range-rate */
		calculateObs(julUTC, position, velocity, gsPos, squintVector, satPos);
		//
		/* Calculate satellite Lat North, Lon East and Alt. */

		calculateLatLonAlt(julUTC, position, satPos);

		satPos.setTime(date);
	}

	/**
	 * Calculate_User_PosVel() passes the user'S observer position and the
	 * time of interest and returns the ECI position and velocity of the
	 * observer. The velocity calculation assumes the observer position is
	 * stationary relative to the earth'S surface.
	 *
	 * Reference: The 1992 Astronomical Almanac, page K11.
	 * 
	 * @param time the time
	 * @param gsPos the ground station position
	 * @param obsPos the position of the observer
	 * @param obsVel the velocity of the observer
	 */
	private static synchronized void calculateUserPosVel(final double time,
			final GroundStationPosition gsPos, final Vector4 obsPos, final Vector4 obsVel) {
		

		double c;
		double sq;
		double achcp;

		gsPos.setTheta(mod2PI(thetaGJD(time) + Satellite.DEG2RAD
				* gsPos.getLongitude()));
		c = 1 / Math.sqrt(1 + FLATTENING_FACTOR * (FLATTENING_FACTOR - 2)
				* sqr(Math.sin(Satellite.DEG2RAD * gsPos.getLatitude())));
		sq = sqr(1 - FLATTENING_FACTOR) * c;
		achcp = (EARTH_RADIUS_KM * c + gsPos.getHeightAMSL())
				* Math.cos(Satellite.DEG2RAD * gsPos.getLatitude());
		obsPos.setX(achcp * Math.cos(gsPos.getTheta()));
		obsPos.setY(achcp * Math.sin(gsPos.getTheta()));
		obsPos.setZ((EARTH_RADIUS_KM * sq + gsPos.getHeightAMSL())
				* Math.sin(Satellite.DEG2RAD * gsPos.getLatitude()));
		obsVel.setX(-MFACTOR * obsPos.getY());
		obsVel.setY(MFACTOR * obsPos.getX());
		obsVel.setZ(0);
		magnitude(obsPos);
		magnitude(obsVel);
	}



	/**
	 *  The procedures Calculate_Obs and Calculate_RADec calculate
	 * thetopocentric coordinates of the object with ECI position,
	 * {pos}, and velocity, {vel}, from location {geodetic} at {time}.
	 * The {obs_set} returned for Calculate_Obs consists of azimuth,
	 * elevation, range, and range rate (in that order) with units of
	 * radians, radians, kilometers, and kilometers/second, respectively.
	 * The WGS '72 geoid is used and the effect of atmospheric refraction
	 * (under standard temperature and pressure) is incorporated into the
	 * elevation calculation; the effect of atmospheric refraction on
	 * range and range rate has not yet been quantified.

	 * The {obs_set} for Calculate_RADec consists of right ascension and
	 * declination (in that order) in radians. Again, calculations are
	 * based ontopocentric position using the WGS '72 geoid and
	 * incorporating atmospheric refraction.
	 * 
	 * @param julUTC Julian date of UTC
	 * @param position the position vector
	 * @param velocity the velocity vector
	 * @param gsPos the ground tstation position
	 * @param squintVector the squint vector
	 * @param satPos the satellite position
	 * 
	 */
	private static synchronized void calculateObs(final double julUTC,
			final Vector4 position, final Vector4 velocity, final GroundStationPosition gsPos,
			final Vector4 squintVector, final SatPos satPos) {

		double sinLat;
		double cosLat;
		double sinTheta;
		double cosTheta;
		double el;
		double azim;
		double topS;
		double topE;
		double topZ;

		final Vector4 obsPos = new Vector4();
		final Vector4 obsVel = new Vector4();
		final Vector4 range = new Vector4();
		final Vector4 rgvel = new Vector4();

		calculateUserPosVel(julUTC, gsPos, obsPos, obsVel);

		range.setX(position.getX() - obsPos.getX());
		range.setY(position.getY() - obsPos.getY());
		range.setZ(position.getZ() - obsPos.getZ());

		/* Save these values globally for calculating squint angles later... */

		squintVector.setX(range.getX());
		squintVector.setY(range.getY());
		squintVector.setZ(range.getZ());

		rgvel.setX(velocity.getX() - obsVel.getX());
		rgvel.setY(velocity.getY() - obsVel.getY());
		rgvel.setZ(velocity.getZ() - obsVel.getZ());

		magnitude(range);

		sinLat = Math.sin(Satellite.DEG2RAD * gsPos.getLatitude());
		cosLat = Math.cos(Satellite.DEG2RAD * gsPos.getLatitude());
		sinTheta = Math.sin(gsPos.getTheta());
		cosTheta = Math.cos(gsPos.getTheta());
		topS = sinLat * cosTheta * range.getX() + sinLat * sinTheta
				* range.getY() - cosLat * range.getZ();
		topE = -sinTheta * range.getX() + cosTheta * range.getY();
		topZ = cosLat * cosTheta * range.getX() + cosLat * sinTheta
				* range.getY() + sinLat * range.getZ();
		azim = Math.atan(-topE / topS);

		if (topS > 0.0) {
			azim = azim + Math.PI;
		}

		if (azim < 0.0) {
			azim = azim + TWO_PI;
		}

		el = Math.asin(topZ / range.getW());

		satPos.setAzimuth(azim);
		satPos.setElevation(el);
		satPos.setRange(range.getW());
		satPos.setRangeRate(dot(range, rgvel) / range.getW());

		final int sector = (int)(satPos.getAzimuth() / TWO_PI
				* 360.0 / 10.0);

		if (gsPos.getHorizonElevations()[sector] > satPos.getElevation()) {
			satPos.setAboveHorizon(false);
		}
	}

	/**
	 * This function returns true if the satellite can ever rise above the
	 * horizon of the ground station.
	 * 
	 * @param qth the ground station position
	 * @return boolean whether or not the satellite will be seen
	 */
	public boolean willBeSeen(final GroundStationPosition qth) {
		double lin;
		double sma;
		double apogee;

		if (tle.getMeanmo() < 1e-8) {
			return false;
		}
		else {
			lin = tle.getIncl();

			if (lin >= 90.0) {
				lin = 180.0 - lin;
			}

			sma = 331.25 * Math.exp(Math.log(1440.0 / tle.getMeanmo())
					* (2.0 / 3.0));
			apogee = sma * (1.0 + tle.getEccn()) - EARTH_RADIUS_KM;
			
			return (Math.acos(EARTH_RADIUS_KM
					/ (apogee + EARTH_RADIUS_KM)) + (lin * DEG2RAD)) > Math
					.abs(qth.getLatitude() * DEG2RAD);
		}

	}

	/**
     * @return the s4
     */
    protected synchronized double getS4() {
    	return s4;
    }

	/**
     * @return the qoms24
     */
    protected synchronized double getQoms24() {
    	return qoms24;
    }
    
    /**
     * Checks and adjusts the calculation if the perigee is less tan
     * 156KM.
     */
    protected synchronized void checkPerigee() {
    	s4 = S;
		qoms24 = QOMS2T;

		if (perigee < PERIGEE_156_KM) {
			if (perigee <= 98.0) {
				s4 = 20.0;
			} 
			else {
				s4 = perigee - 78.0;
			}

			qoms24 = Math.pow((120 - s4) * AE / EARTH_RADIUS_KM, 4);
			s4 = s4 / EARTH_RADIUS_KM + AE;
		}
    }

	/**
     * @param perigee the perigee to set
     */
    protected synchronized void setPerigee(final double perigee) {
    	this.perigee = perigee;
    }
}
