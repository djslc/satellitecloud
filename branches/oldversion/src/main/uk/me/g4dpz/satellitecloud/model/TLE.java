/*
 satellitecloud: Satellite orbit prediction web service

 Copyright (C)  2004-2009  David A. B. Johnson, G4DPZ.

 This class is a Java port of one of the core elements of 
 the Predict program, Copyright John A. Magliacane, 
 KD2BD 1991-2003: http://www.qsl.net/kd2bd/predict.html

 Dr. T.S. Kelso is the original author of the SGP4/SDP4 orbital models,
 originally written in Fortran and Pascal, and released into
 the public domain through his website (http://www.celestrak.com/).
 Neoklis Kyriazis, 5B4AZ, later re-wrote Dr. Kelso's code in C,
 and released it under the GNU GPL in 2002.
 PREDICT's core is based on 5B4AZ's code translation efforts.

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

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.StringUtils;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TLE implements Serializable {

	/** Serial version ID. */
	private static final long serialVersionUID = -5194414788388421744L; 

	/** the number of lines in the TLE!. */
	private static transient final int THREE_LINES = 3;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

	@Persistent
    private int catnum;
	
	@Persistent
    private String name;
	
	@Persistent
    private int setnum;
	
	@Persistent
    private int year;
	
	@Persistent
    private double refepoch;
	
	@Persistent
    private double incl;
	
	@Persistent
    private double raan;
	
	@Persistent
    private double eccn;
	
	@Persistent
    private double argper;
	
	@Persistent
    private double meanan;
	
	@Persistent
    private double meanmo;
	
	@Persistent
    private double drag;
	
	@Persistent
    private double nddot6;
	
	@Persistent
    private double bstar;
	
	@Persistent
    private int orbitnum;
	
	@Persistent
    private boolean deepspace;
	
	@Persistent
    private java.util.Date createddate;
	
	private transient double epoch;
	private transient double xndt2o;
	private transient double xincl;
	private transient double xnodeo;
	private transient double eo;
	private transient double omegao;
	private transient double xmo;
	private transient double xno;
	/**
	 * @param id
	 * @param catnum
	 * @param name
	 * @param setnum
	 * @param year
	 * @param refepoch
	 * @param incl
	 * @param raan
	 * @param eccn
	 * @param argper
	 * @param meanan
	 * @param meanmo
	 * @param drag
	 * @param nddot6
	 * @param bstar
	 * @param orbitnum
	 * @param deepspace
	 * @param createddate
	 */
	public TLE(Long id, int catnum, String name, int setnum, int year,
			double refepoch, double incl, double raan, double eccn,
			double argper, double meanan, double meanmo, double drag,
			double nddot6, double bstar, int orbitnum, boolean deepspace,
			Date createddate) {
		this.id = id;
		this.catnum = catnum;
		this.name = name;
		this.setnum = setnum;
		this.year = year;
		this.refepoch = refepoch;
		this.incl = incl;
		this.raan = raan;
		this.eccn = eccn;
		this.argper = argper;
		this.meanan = meanan;
		this.meanmo = meanmo;
		this.drag = drag;
		this.nddot6 = nddot6;
		this.bstar = bstar;
		this.orbitnum = orbitnum;
		this.deepspace = deepspace;
		this.createddate = createddate;
		
		this.epoch = epoch;
		this.xndt2o = xndt2o;
		this.xincl = xincl;
		this.xnodeo = xnodeo;
		this.eo = eo;
		this.omegao = omegao;
		this.xmo = xmo;
		this.xno = xno;
	}
	
	/** 
	 * Constructor. 
	 * 
	 * @param tle the three line elements
	 * @throws IllegalArgumentException here was something wrong with the TLE
	 */
	public TLE(final String[] tle) throws IllegalArgumentException {
		
		if (null == tle) {
			throw new IllegalArgumentException("TLE was null");
		}

		if (tle.length != THREE_LINES) {
			throw new IllegalArgumentException("TLE had " + tle.length
			        + " elements");
		}

		int lineCount = 0;

		for (String line : tle) {
			
			testArguments(lineCount, line);

			lineCount++;
		}

		catnum = Integer.parseInt(StringUtils.strip(tle[1].substring(2, 7)));
		name = tle[0].trim();
		setnum = Integer.parseInt(StringUtils.strip(tle[1].substring(64, 68)));
		year = Integer.parseInt(StringUtils.strip(tle[1].substring(18, 20)));
		refepoch = Double.parseDouble(tle[1].substring(20, 32));
		incl = Double.parseDouble(tle[2].substring(8, 16));
		raan = Double.parseDouble(tle[2].substring(17, 25));
		eccn = 1.0e-07 * Double.parseDouble(tle[2].substring(26, 33));
		argper = Double.parseDouble(tle[2].substring(34, 42));
		meanan = Double.parseDouble(tle[2].substring(43, 51));
		meanmo = Double.parseDouble(tle[2].substring(52, 63));
		drag = Double.parseDouble(tle[1].substring(33, 43));

		nddot6 = 1.0e-5 * Double.parseDouble(tle[1].substring(44, 50))
		        / Math.pow(10.0, Double.parseDouble(tle[1].substring(51, 52)));

		bstar = 1.0e-5 * Double.parseDouble(tle[1].substring(53, 59))
		        / Math.pow(10.0, Double.parseDouble(tle[1].substring(60, 61)));

		bstar /= Satellite.AE;

		orbitnum = Integer.parseInt(StringUtils.strip(tle[2].substring(63, 68)));

		/* reassign the values to thse which get used in calculations */
		epoch = (1000.0 * getYear()) + getRefepoch();

		xndt2o = drag;

		xincl = incl * Satellite.DEG2RAD;

		xnodeo = raan * Satellite.DEG2RAD;
		
		eo = eccn;

		omegao = argper * Satellite.DEG2RAD;

		xmo = meanan * Satellite.DEG2RAD;

		xno = meanmo;

		/* Preprocess tle set */

		preProcessTLESet();
	}
	


	/**
     * @return the id
     */
    public synchronized final long getId() {
    	return id;
    }

	/**
     * @param id the id to set
     */
    public synchronized final void setId(long id) {
    	this.id = id;
    }
	

	/**
	 * @return the catalog number
	 */
	public int getCatnum() {
		return this.catnum;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the element set number
	 */
	public int getSetnum() {
		return this.setnum;
	}

	/**
	 * @return the year part of the date of the elements
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * @return the reference epoch of the elements
	 */
	public double getRefepoch() {
		return this.refepoch;
	}

	/**
	 * @return the inclination of the satellite orbit
	 */
	public double getIncl() {
		return this.incl;
	}

	/**
	 * @return the Right Ascention of the Acending Node of the orbit
	 */
	public double getRaan() {
		return this.raan;
	}

	/**
	 * @return the Eccentricity of the orbit
	 */
	public double getEccn() {
		return this.eccn;
	}

	/**
	 * @return the Argument of Perigee of the orbit
	 */
	public double getArgper() {
		return this.argper;
	}

	/**
	 * @return the Mean Anomoly of the orbit
	 */
	public double getMeanan() {
		return this.meanan;
	}

	/**
	 * @return the Mean Motion of the satellite
	 */
	public double getMeanmo() {
		return this.meanmo;
	}

	/**
	 * @return the Drag factor
	 */
	public double getDrag() {
		return this.drag;
	}

	/**
	 * @return Nddot6
	 */
	public double getNddot6() {
		return this.nddot6;
	}

	/**
	 * @return Bstar
	 */
	public double getBstar() {
		return this.bstar;
	}

	/**
	 * @return Orbitnum
	 */
	public int getOrbitnum() {
		return this.orbitnum;
	}

	/**
	 * @return Deepspace
	 */
	public boolean isDeepspace() {
		return deepspace;
	}

	/**
	 * @return Eo
	 */
	public double getEo() {
		return eo;
	}

	/**
	 * @return Epoch
	 */
	public double getEpoch() {
		return epoch;
	}

	/**
	 * @return Omegao
	 */
	public double getOmegao() {
		return omegao;
	}

	/**
	 * @return Xincl
	 */
	public double getXincl() {
		return xincl;
	}

	/**
	 * @return Xmo
	 */
	public double getXmo() {
		return xmo;
	}

	/**
	 * @return Xndt2o
	 */
	public double getXndt2o() {
		return xndt2o;
	}

	/**
	 * @return Xno
	 */
	public double getXno() {
		return xno;
	}

	/**
	 * @return Xnodeo
	 */
	public double getXnodeo() {
		return xnodeo;
	}

	/**
	 * @return the createddate
	 */
	public Date getCreateddate() {
		return new Date(createddate.getTime());
	}

	/**
	 * @param createddate the createddate to set
	 */
	public void setCreateddate(final Date createddate) {
		this.createddate = new Date(createddate.getTime());
	}

	

	/**
     * 
     */
    private synchronized void preProcessTLESet() {
	    double temp;
	    temp = Satellite.TWO_PI / Satellite.MINS_PER_DAY / Satellite.MINS_PER_DAY;
		xno = xno * temp * Satellite.MINS_PER_DAY;
		xndt2o *= temp;

		double dd1 = Satellite.XKE / xno;
		final double a1 = Math.pow(dd1, Satellite.TWO_THIRDS);
		final double r1 = Math.cos(xincl);
		dd1 = 1.0 - eo * eo;
		temp = Satellite.CK2 * 1.5f * (r1 * r1 * 3.0 - 1.0)
		        / Math.pow(dd1, 1.5);
		final double del1 = temp / (a1 * a1);
		final double ao = a1
		        * (1.0 - del1
		                * (Satellite.TWO_THIRDS * .5 + del1
		                        * (del1 * 1.654320987654321 + 1.0)));
		final double delo = temp / (ao * ao);
		final double xnodp = xno / (delo + 1.0);

		/* Select a deep-space/near-earth ephemeris */

		deepspace = Satellite.TWO_PI / xnodp / Satellite.MINS_PER_DAY >= 0.15625;
    }

	/**
     * @param lineCount the current line
     * @param line the line under test
     * @throws IllegalArgumentException there was a problem with the data
     */
    private void testArguments(final int lineCount, final String line)
    	throws IllegalArgumentException {
	    if (null == line) {
	    	throw new IllegalArgumentException(
	    		createIllegalArgumentMessage(lineCount, "was null"));
	    }

	    if (0 == line.length()) {
	    	throw new IllegalArgumentException(
	    			createIllegalArgumentMessage(lineCount, "was zero length"));
	    }
    }

	/**
     * @param lineCount the line count
     * @param problem the problem
     * @return the description
     */
    private String createIllegalArgumentMessage(final int lineCount, final String problem) {
	    return "TLE line[" + lineCount
	            + "] " + problem;
    }

}
