/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package uk.me.g4dpz.gae.satellitecloud.dto;

import java.sql.Date;

import com.spoledge.audao.db.dto.AbstractDto;

/**
 * This is a DTO class.
 *
 * @author generated
 */
public class SatelliteElementSet extends AbstractDto {

    ////////////////////////////////////////////////////////////////////////////
    // Static
    ////////////////////////////////////////////////////////////////////////////

    public static final String TABLE = "SatelliteElementSet";

    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

    private Long catalogNumber;
    private String name;
    private String line1;
    private String line2;
    private Long setNumber;
    private Date createdDate;
    private Date updatedDate;

    private boolean isUpdatedDateModified;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new empty DTO.
     */
    public SatelliteElementSet() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    public Long getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber( Long _val) {
        this.catalogNumber = _val;
    }

    public String getName() {
        return name;
    }

    public void setName( String _val) {
        this.name = _val;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1( String _val) {
        this.line1 = _val;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2( String _val) {
        this.line2 = _val;
    }

    public Long getSetNumber() {
        return setNumber;
    }

    public void setSetNumber( Long _val) {
        this.setNumber = _val;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate( java.util.Date _val ) {
        setCreatedDate((Date)( _val != null ? new Date( _val.getTime()) : null ));
    }

    public void setCreatedDate( Date _val) {
        this.createdDate = _val;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate( java.util.Date _val ) {
        setUpdatedDate((Date)( _val != null ? new Date( _val.getTime()) : null ));
    }

    public void setUpdatedDate( Date _val) {
        this.updatedDate = _val;
        this.isUpdatedDateModified = true;
    }

    public boolean isUpdatedDateModified() {
        return isUpdatedDateModified;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected void contentToString(StringBuffer sb) {
        append( sb, "catalogNumber", catalogNumber );
        append( sb, "name", name );
        append( sb, "line1", line1 );
        append( sb, "line2", line2 );
        append( sb, "setNumber", setNumber );
        append( sb, "createdDate", createdDate );
        append( sb, "updatedDate", updatedDate );
    }
}
