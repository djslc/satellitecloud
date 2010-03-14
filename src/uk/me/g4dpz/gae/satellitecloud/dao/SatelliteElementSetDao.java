/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package uk.me.g4dpz.gae.satellitecloud.dao;

import java.sql.Date;
import java.sql.Timestamp;

import com.spoledge.audao.db.dao.AbstractDao;
import com.spoledge.audao.db.dao.DaoException;

import uk.me.g4dpz.gae.satellitecloud.dto.SatelliteElementSet;


/**
 * This is the DAO.
 *
 * @author generated
 */
public interface SatelliteElementSetDao extends AbstractDao {

    /**
     * Finds a record identified by its primary key.
     * @return the record found or null
     */
    public SatelliteElementSet findByPrimaryKey( long catalogNumber );

    /**
     * Finds a record.
     */
    public SatelliteElementSet findByCatalogNumber( long catalogNumber );

    /**
     * Inserts a new record.
     */
    public void insert( SatelliteElementSet dto ) throws DaoException;

    /**
     * Updates one record found by primary key.
     * @return true iff the record was really updated (=found and any change was really saved)
     */
    public boolean update( long catalogNumber, SatelliteElementSet dto ) throws DaoException;

}