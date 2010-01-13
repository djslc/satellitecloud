/*
 * This file was generated - do not edit it directly !!
 * Generated by AuDAO 2009 tool, a product of Spolecne s.r.o.
 * For more information please visit http://www.spoledge.com
 */
package uk.me.g4dpz.appsengine.satellitecloud.jdo.dao;



/**
 * This is the main class for obtaining DAO objects.
 * It looks for the system property called "uk.me.g4dpz.appsengine.satellitecloud.jdo.dao.DB_TYPE"
 * and its value is used as the DAO implementation subpackage name.
 * The default value is "gae".
 * The name of the Factory class is assumed to be "DaoFactoryImpl"
 * and it must have the default constructor defined.
 *
 * @author generated
 */
public class DaoFactory {

    private static Factory factory;

    static {
        String pkgDao = DaoFactory.class.getPackage().getName();
        String pkgImpl = pkgDao + '.' + System.getProperty( pkgDao + ".DB_TYPE", "gae" );
        try {
            Class<?> aclazz = Class.forName( pkgImpl + ".DaoFactoryImpl" );
            Class<? extends Factory> clazz = aclazz.asSubclass( Factory.class );
            factory = clazz.newInstance();
        }
        catch (Exception e) {
            throw new Error("A problem occurred when resolving DAO factory class", e);
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    // Static methods
    ////////////////////////////////////////////////////////////////////////////

    public static SatelliteElementDao createSatelliteElementDao() {
        return factory.createSatelliteElementDao();
    }

    public static SatelliteGroupDao createSatelliteGroupDao() {
        return factory.createSatelliteGroupDao();
    }


    ////////////////////////////////////////////////////////////////////////////
    // Inner classes
    ////////////////////////////////////////////////////////////////////////////

    public static abstract class Factory {

        public abstract SatelliteElementDao createSatelliteElementDao();

        public abstract SatelliteGroupDao createSatelliteGroupDao();

    }
}
