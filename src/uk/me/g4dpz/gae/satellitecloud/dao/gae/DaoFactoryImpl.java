/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package uk.me.g4dpz.gae.satellitecloud.dao.gae;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import uk.me.g4dpz.gae.satellitecloud.dao.*;


/**
 * This is the main implementation class for obtaining DAO objects.
 * @author generated
 */
public class DaoFactoryImpl extends DaoFactory.Factory {
    public SatelliteElementSetDao createSatelliteElementSetDao( DatastoreService ds ) {
        return new SatelliteElementSetDaoImpl( ds );
    }

}
