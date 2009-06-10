package uk.me.g4dpz.satellitecloud.model;

/*
 WebSat: Satellite orbit prediction web service

 Copyright (C)  2004-2009  David A. B. Johnson, G4DPZ.

 Authors: David A. B. Johnson, G4DPZ <dave@g4dpz.me.uk>

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

import java.io.Serializable;

/**
 * A general purpose Vector with 4 entries.
 *
 * @author g4dpz
 */
public class Vector4 extends Object implements Serializable {

	/** serialized id. */
	private static final long serialVersionUID = -8804649332186066551L;

	/** the w part of the vector. `*/
	private double w;
	/** the x part of the vector. `*/
	private double x;
	/** the y part of the vector. `*/
	private double y;
	/** the z part of the vector. `*/
	private double z;

	/** default constructor. */
	Vector4() {
		this.w = 0.0;
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}

	/**
	 * @param w the w value
	 * @param x the x value
	 * @param y the y value
	 * @param z the z value
	 */
	Vector4(final double w, final double x, final double y, final double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets the string representation of the object.
	 * @return the string representation of the object
	 */
	@Override
	public final String toString() {
		return "w: " + w + ", x: " + x + ", y: " + y + ", z: " + z;
	}

	/**
	 * @return the w
	 */
	public final synchronized double getW() {
		return w;
	}

	/**
	 * @param w
	 *            the w to set
	 */
	public final synchronized void setW(final double w) {
		this.w = w;
	}

	/**
	 * @return the x
	 */
	public final synchronized double getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public final synchronized void setX(final double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public final synchronized double getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public final synchronized void setY(final double y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public final synchronized double getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public final synchronized void setZ(final double z) {
		this.z = z;
	}
}
