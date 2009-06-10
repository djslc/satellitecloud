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

/**
 * The location of the Satellite Grund Station.
 * @author g4dpz
 */
public class GroundStationPosition {
	private double latitude;
	private double longitude;
	private double heightAMSL;
	private double theta;
	private int[] horizonElevations = new int[36];

	/**
	 * @param latitude the latitue of the ground station in degrees, North: positive
	 * @param longitude the longitude of the ground station in degrees, East: positive
	 * @param heightAMSL the height of te ground station above mean sea level, in metres
	 */
	public GroundStationPosition(final double latitude, final double longitude,
			final double heightAMSL) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.heightAMSL = heightAMSL;
	}

	/**
	 * Default constructor.
	 */
	public GroundStationPosition() {
	}

	/**
	 * @return latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return elevation
	 */
	public double getHeightAMSL() {
		return heightAMSL;
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
	 * @return the horizonElevations
	 */
	public final int[] getHorizonElevations() {
		return horizonElevations.clone();
	}

	/**
	 * The horizonElevations to set.
	 * 
	 * @param horizonElevations the list of horizontal elevations
	 * @throws IllegalArgumentException the input arguments were incorrect
	 */
	public final void setHorizonElevations(final int[] horizonElevations)
		throws IllegalArgumentException {

		final int elevations = horizonElevations.length;

		if (36 != elevations) {
			throw new IllegalArgumentException(
					"Expected 36 Horizon Elevations, got: " + elevations);
		}

		this.horizonElevations = horizonElevations.clone();
	}

}
