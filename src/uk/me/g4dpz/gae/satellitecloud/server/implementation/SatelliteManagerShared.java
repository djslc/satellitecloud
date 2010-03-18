package uk.me.g4dpz.gae.satellitecloud.server.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import uk.me.g4dpz.gae.satellitecloud.dao.SatelliteElementSetDao;
import uk.me.g4dpz.gae.satellitecloud.dao.gae.DaoFactoryImpl;
import uk.me.g4dpz.gae.satellitecloud.dto.SatelliteElementSet;
import uk.me.g4dpz.satellite.Satellite;
import uk.me.g4dpz.satellite.SatelliteFactory;
import uk.me.g4dpz.satellite.TLE;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.spoledge.audao.db.dao.DaoException;

public class SatelliteManagerShared {
	
    private static final String AS_SITE_ID_FOR_DOWLOADING_KEPS = " as site id for dowloading KEPS";
	private static final String BADGERJOHNSON_GMAIL_COM = "badgerjohnson@googlemail.com";
	private static final Logger LOGGER = Logger.getLogger(SatelliteManagerShared.class.getName());
	private static final String NEW_LINE = "\n";
	private static final int KEPS_EXPIRY = 7200;
	private MemcacheService satelliteCacheService;
	private MemcacheService predictionCacheService;
	private DaoFactoryImpl daoFactory;
	private DatastoreService ds;
	private SatelliteElementSetDao satelliteElementSetDao;
	MailService mailService;
	private String[] lines;
	
	
	private static SimpleTimeZone TZ = new SimpleTimeZone(0, "UTC");
	
	public SatelliteManagerShared() {
		daoFactory = new DaoFactoryImpl();
		ds = DatastoreServiceFactory.getDatastoreService();
		satelliteElementSetDao = daoFactory.createSatelliteElementSetDao(ds);
    	satelliteCacheService = MemcacheServiceFactory.getMemcacheService();
    	mailService = MailServiceFactory.getMailService();
    	
		lines = new String[3];
	}

	public Boolean loadKeps(final String id, final String fileName) {		
		
		String msgBody = "";
        
        String kepsFileName = (null != fileName) ? fileName : "amateur.txt";
        
        boolean success = false;
    	
		SimpleTimeZone zone = new SimpleTimeZone(0, "UTC");
    	
    	List<SatelliteElementSet> elementSets;
    	
    	Transaction tx = null;

        try {
            
            String url = "";
            
    		msgBody = "Using: " + id + "/" + kepsFileName + AS_SITE_ID_FOR_DOWLOADING_KEPS + NEW_LINE;
        	
    		
        	if (id.equals(KeplerSource.celestrak.name())) {
        		url = "http://www.celestrak.com/NORAD/elements/" + kepsFileName;
        	}
        	else if (id.equals(KeplerSource.localhost.name())) {
        		url = "http://localhost/~badger/" + kepsFileName;
        	}
        	else if (id.equals(KeplerSource.spacelink.name())) {
        		// TODO implement spacelink upload
        	}
        	else {
        		throw new IllegalArgumentException(
        			"Tried to use: " + id + AS_SITE_ID_FOR_DOWLOADING_KEPS);
        	}
    		
        	elementSets = parseTLE(url);
    		
    		msgBody += "Elements read: " + elementSets.size() + NEW_LINE;
    		
    		if (null == ds) {
    			throw new NullPointerException("Could not create DatastoreService");
    		}
    		
			for (SatelliteElementSet satSet : elementSets) {
    			
    			tx = ds.beginTransaction();
        		
        		if (null == tx) {
        			throw new NullPointerException("Could not create Transaction");
        		}
    			
    			Satellite cachedSatellite = (Satellite)satelliteCacheService.get(satSet.getCatalogNumber());
    			
    			if (null == cachedSatellite) {
    				
    				msgBody += ("Satellite: " + satSet.getName() + " was not in cache"+ NEW_LINE);
    				
    				SatelliteElementSet satSetDb = 
    					satelliteElementSetDao.findByPrimaryKey(satSet.getCatalogNumber());
    				
    				if (null == satSetDb) {
    					
    					msgBody += ("Satellite: " + satSet.getName() + " was not in database"+ NEW_LINE);
    					satelliteElementSetDao.insert(satSet);
        				msgBody += ("Added satellite: " + satSet.getName() + NEW_LINE);
        				
        			} else {
        				
        				msgBody += ("Satellite: " + satSet.getName() + " was in database"+ NEW_LINE);
        				msgBody += ("Comparing satSet for: " + satSet.getName() + " with tle on SetNum: " + satSetDb.getSetNumber() + ", " + satSet.getSetNumber() + NEW_LINE);
        				
        				if (satSet.getSetNumber().longValue() > satSetDb.getSetNumber().longValue()) {
        					
        					satSetDb.setName(satSet.getName());
        					satSetDb.setLine1(satSet.getLine1());
        					satSetDb.setLine2(satSet.getLine2());
        					satSetDb.setSetNumber(satSet.getSetNumber());
    	    				satSetDb.setUpdatedDate(Calendar.getInstance(zone).getTime());	
    	    				satelliteElementSetDao.update(satSetDb.getCatalogNumber(), satSetDb);
    	    				msgBody += ("Updated satellite: " + satSet.getName() + NEW_LINE);
    	    				
        				}
        			}
    				updateCache(satSet);
    			} else {
    				int setNumber = cachedSatellite.getTLE().getSetnum();
    				msgBody += ("Satellite: " + satSet.getName() + " was in cache"+ NEW_LINE);
    				msgBody += ("Comparing satSet for: " + satSet.getName() + " with tle on SetNum: " + setNumber + ", " + satSet.getSetNumber() + NEW_LINE);
    				if (satSet.getSetNumber().longValue() > setNumber) {
	    				satelliteElementSetDao.update(satSet.getSetNumber(), satSet);
	        			msgBody += ("Updated satellite: " + satSet.getName() + NEW_LINE);
	    				updateCache(satSet);
    				}
    			}
    			
				tx.commit();
    		}
    		
    		success = true;

        } catch (IOException e) {
        	msgBody = reportError(e, msgBody);
		} catch (NullPointerException e) {
        	msgBody = reportError(e, msgBody);
		} catch (DaoException e) {
        	msgBody = reportError(e, msgBody);
		} finally {
			if (null != tx && tx.isActive()) {
                tx.rollback();
            }
		}
        
    	sendMailMessage(msgBody);
        
		return Boolean.valueOf(success);
		
	}

	/**
	 * @param msgBody
	 */
	private void sendMailMessage(String msgBody) {
		MailService.Message mess = new MailService.Message();
    	mess.setSender(BADGERJOHNSON_GMAIL_COM);
    	mess.setTo("david.johnson@blackpepper.co.uk");
    	mess.setSubject("loadKeps called");
    	mess.setTextBody(msgBody);
    
    	try {
			mailService.send(mess);
		} catch (IOException e) {
        	LOGGER.severe(e.getMessage());
		}
	}

	private void updateCache(SatelliteElementSet satSet) {
		lines[0] = satSet.getName();
		lines[1] = satSet.getLine1();
		lines[2] = satSet.getLine2();
		Satellite satellite = SatelliteFactory.createSatellite(new TLE(lines));
		satelliteCacheService.put(satSet.getCatalogNumber(), satellite, Expiration.byDeltaSeconds(KEPS_EXPIRY));
	}

	private String reportError(final Exception e, final String msgBody) {
		StringBuilder sb = new StringBuilder(msgBody);
		sb.append(e.getMessage());
		LOGGER.setLevel(Level.SEVERE);
        LOGGER.severe(msgBody);
		return sb.toString();
	}
	
	/**
     * Parses the TLE String.
     * 
     * @param tleString
     *            The TLE string
	 * @param msgBody 
     * @return the map of name to TLE
     * @throws IOException
     *             problem processing the file
     */
    private List<SatelliteElementSet> parseTLE(String urlString) throws IOException
    {
        List<SatelliteElementSet> elementSets = new ArrayList<SatelliteElementSet>();
        BufferedReader reader = null;
        
        try
        {
            String line = null;
            int i = 0;
            URL url = new URL(urlString);
            String name = null;
            String line1 = null;
            String line2 = null;

            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = reader.readLine()) != null)
            {
                i++;
                switch (i)
                {
                    case 1:
                    {
                        name = line.trim();
                        break;
                    }
                    case 2:
                    {
                        line1 = line;
                        break;
                    }
                    case 3:
                    {
                    	line2 = line;
                    	Long catnum = Long.parseLong(StringUtils.strip(line1.substring(2, 7)));
                    	long setnum = Long.parseLong(StringUtils.strip(line1.substring(64, 68)));
                    	SatelliteElementSet satSet = new SatelliteElementSet();
                    	satSet.setCatalogNumber(catnum);
                    	satSet.setName(name);
                    	satSet.setLine1(line1);
                    	satSet.setLine2(line2);
                    	satSet.setSetNumber(setnum);
                    	satSet.setCreatedDate(Calendar.getInstance(TZ).getTime());
                    	elementSets.add(satSet);
                        i = 0;
                        break;
                    }
                    default:
                    {
                        throw new IOException("TLE string did not contain three elements");
                    }
                }
            }
        }
        finally {
        	if (null != reader) {
        		reader.close();
        	}
        }
        
        return elementSets;
    }

	public Boolean preLoadSatellitePredictions(String hours, String seconds) {
		
		StringBuilder sb = new StringBuilder("Preloading satellite predictions: ").append(NEW_LINE);
		
		if (null == predictionCacheService) {
			predictionCacheService = MemcacheServiceFactory.getMemcacheService();
			sb.append("Creating new prediction cache");
		} else {
			predictionCacheService.clearAll();
			sb.append("Clearing prediction cache");
		}
		
		sendMailMessage(sb.toString());
		
		return true;
	}

}
