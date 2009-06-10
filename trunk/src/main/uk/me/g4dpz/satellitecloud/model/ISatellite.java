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
 * @author badger
 * 
 */
public interface ISatellite {

	/**
	 * Gets the satellite position.
	 * @param gsPos the ground statin position
	 * @param satPos the satellite position
	 * @param date the date at which to do the calculation
	 */
	void getPosition(GroundStationPosition gsPos, SatPos satPos,
			Date date);

	/**
	 * Determines if the satellite will be seen.
	 * @param qth the ground station position
	 * @return if the satellite will be seen
	 */
	boolean willBeSeen(GroundStationPosition qth);

}
