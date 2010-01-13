/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO 2009 tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package uk.me.g4dpz.appsengine.satellitecloud.jdo.dto;

import com.spoledge.audao.db.dto.AbstractDto;

/**
 * This is a DTO class.
 *
 * @author generated
 */
public class SatelliteElement extends AbstractDto {

    ////////////////////////////////////////////////////////////////////////////
    // Static
    ////////////////////////////////////////////////////////////////////////////

    public static final String TABLE = "SatelliteElement";

    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

    private Long id;
    private Long catalogueNumber;
    private Long elementSet;
    private Long groupId;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new empty DTO.
     */
    public SatelliteElement() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId( Long _val) {
        this.id = _val;
    }

    public Long getCatalogueNumber() {
        return catalogueNumber;
    }

    public void setCatalogueNumber( Long _val) {
        this.catalogueNumber = _val;
    }

    public Long getElementSet() {
        return elementSet;
    }

    public void setElementSet( Long _val) {
        this.elementSet = _val;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId( Long _val) {
        this.groupId = _val;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected void contentToString(StringBuffer sb) {
        append( sb, "id", id );
        append( sb, "catalogueNumber", catalogueNumber );
        append( sb, "elementSet", elementSet );
        append( sb, "groupId", groupId );
    }
}
