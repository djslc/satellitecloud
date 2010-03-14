package uk.me.g4dpz.gae.satellitecloud.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import uk.me.g4dpz.satellite.TLE;

@Entity
public class SatelliteElementSet implements Serializable {
	
	private static final long serialVersionUID = -8352791841591549854L;
	
	@Id
	private Long catalogNumber;
	private String name;
	private String line1;
	private String line2;
	private Long setNumber;
	private Date createdDate;
	private Date updatedDate;

	public SatelliteElementSet() {
		// TODO Auto-generated constructor stub
	}
	
	public SatelliteElementSet(
			final Long catalogNumber,
			final String[] lines,
			final Long setNumber,
			final Date createdDate) {
		this.catalogNumber = catalogNumber;
		this.name = lines[0].trim();
		this.line1 = lines[1];
		this.line2 = lines[2];
		this.setNumber = setNumber;
		this.createdDate = createdDate;
	}
	
	public void update(final SatelliteElementSet elementSet, final Date updatedDate) {
		this.name = elementSet.name;
		this.line1 = elementSet.line1;
		this.line2 = elementSet.line2;
		this.setNumber = elementSet.setNumber;
		this.updatedDate = updatedDate;
	}

	public Long getCatalogNumber() {
		return catalogNumber;
	}

	public void setCatalogNumber(Long catalogNumber) {
		this.catalogNumber = catalogNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the line1
	 */
	public final String getLine1() {
		return line1;
	}

	/**
	 * @param line1 the line1 to set
	 */
	public final void setLine1(String line1) {
		this.line1 = line1;
	}

	/**
	 * @return the line2
	 */
	public final String getLine2() {
		return line2;
	}

	/**
	 * @param line2 the line2 to set
	 */
	public final void setLine2(String line2) {
		this.line2 = line2;
	}

	public Long getSetNumber() {
		return setNumber;
	}

	public void setSetNumber(Long setNumber) {
		this.setNumber = setNumber;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


}
