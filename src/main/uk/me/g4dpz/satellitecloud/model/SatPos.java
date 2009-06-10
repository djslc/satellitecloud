/*
 WebSat: Satellite orbit prediction web service

 Copyright (C)  2004-2009  David A. B. Johnson, G4DPZ.

 Author: David A. B. Johnson, G4DPZ <dave@g4dpz.me.uk>

 Comments, questions and bug reports should be submitted via
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
package uk.me.g4dpz.satellitecloud.model;

import java.util.Date;

/**
 * 
 * @author g4dpz
 *
 */
public class SatPos {
	private static final String DEG_CR = " deg.\n";
	// the internal representation will be in radians
	private double azimuth;
	private double elevation;
	private double latitude;
	private double longitude;

	private Date time;
	private double range;
	private double rangeRate;
	private double phase;
	private double altitude;
	private double theta;

	private boolean aboveHorizon;

	/**
	 * Default constructor.
	 */
	public SatPos() {

	}

	/**
	 * Constructs a Satellite Position.
	 * @param azimuth the Azimuth
	 * @param elevation the Elevation
	 * @param time the Time
	 */
	public SatPos(final double azimuth, final double elevation, final Date theTime) {
		this.azimuth = azimuth;
		this.elevation = elevation;
		this.time = new Date();
		this.time.setTime(theTime.getTime());
	}

	/**
	 * @return the azimuth
	 */
	public double getAzimuth() {
		return azimuth;
	}

	/**
	 * @return the elevation
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * @return time for the SatPos
	 */
	public Date getTime() {
		return new Date(time.getTime());
	}

	/**
	 * @return the range
	 */
	public final double getRange() {
		return range;
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public final void setRange(final double range) {
		this.range = range;
	}

	/**
	 * @return the rangeRate
	 */
	public final double getRangeRate() {
		return rangeRate;
	}

	/**
	 * @param rangeRate
	 *            the rangeRate to set
	 */
	public final void setRangeRate(final double rangeRate) {
		this.rangeRate = rangeRate;
	}

	/**
	 * @return the phase
	 */
	public final double getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public final void setPhase(final double phase) {
		this.phase = phase;
	}

	/**
	 * @return the latitude
	 */
	public final double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public final void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public final double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public final void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the altitude
	 */
	public final double getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public final void setAltitude(final double altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the theta
	 */
	public final double getTheta() {
		return theta;
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public final void setTheta(final double theta) {
		this.theta = theta;
	}

	/**
	 * @param azimuth
	 *            the azimuth to set
	 */
	public final void setAzimuth(final double azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * @param elevation
	 *            the elevation to set
	 */
	public final void setElevation(final double elevation) {
		this.elevation = elevation;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public final void setTime(final Date time) {
		this.time = new Date(time.getTime());
	}

	/**
	 * @return the aboveHorizon
	 */
	public final boolean isAboveHorizon() {
		return aboveHorizon;
	}

	/**
	 * @param aboveHorizon
	 *            the aboveHorizon to set
	 */
	public final void setAboveHorizon(final boolean aboveHorizon) {
		this.aboveHorizon = aboveHorizon;
	}

	/**
	 * @return a pretty printed version of the Satellite Position
	 */
	@Override
	public String toString() {
		return "Azimuth:    " + azimuth / Math.PI * 2.0 * 360 + DEG_CR
				+ "Elevation:  " + elevation / Math.PI * 2.0 * 360 + DEG_CR
				+ "Latitude:   " + latitude / Math.PI * 2.0 * 360 + DEG_CR
				+ "Longitude:  " + longitude / Math.PI * 2.0 * 360 + DEG_CR

				+ "Date:       " + time + "\n" 
				+ "Range:      " + range + " km.\n" 
				+ "Range rate: " + rangeRate + " m/S.\n"
				+ "Phase:      " + phase + " /(256)\n" 
				+ "Altitude:   " + altitude + " km\n" 
				+ "Theta:      " + theta + " rad/sec\n";
	}

}
