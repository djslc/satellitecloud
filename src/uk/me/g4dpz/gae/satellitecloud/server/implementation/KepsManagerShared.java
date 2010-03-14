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

import uk.me.g4dpz.gae.satellitecloud.dao.DaoFactory;
import uk.me.g4dpz.gae.satellitecloud.dao.SatelliteElementSetDao;
import uk.me.g4dpz.gae.satellitecloud.dao.gae.DaoFactoryImpl;
import uk.me.g4dpz.gae.satellitecloud.dto.SatelliteElementSet;
import uk.me.g4dpz.satellite.TLE;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.spoledge.audao.db.dao.DaoException;

public class KepsManagerShared {
	
    private static final String AS_SITE_ID_FOR_DOWLOADING_KEPS = " as site id for dowloading KEPS";
	private static final String BADGERJOHNSON_GMAIL_COM = "badgerjohnson@googlemail.com";
	private static final Logger LOGGER = Logger.getLogger(KepsManagerShared.class.getName());
	private static final String NEW_LINE = "\n";
	private MemcacheService memcacheService = null;
	private DaoFactoryImpl daoFactory;
	private DatastoreService ds;
	private SatelliteElementSetDao satelliteElementSetDao;
	
	
	
	private static SimpleTimeZone TZ = new SimpleTimeZone(0, "UTC");
	
	public KepsManagerShared() {
		daoFactory = new DaoFactoryImpl();
		ds = DatastoreServiceFactory.getDatastoreService();
		satelliteElementSetDao = daoFactory.createSatelliteElementSetDao(ds);
	}

	public Boolean loadKeps(final String id, final String fileName) {		
		
		String msgBody = "";
        
        String kepsFileName = (null != fileName) ? fileName : "amateur.txt";
        
        boolean success = false;
        
    	MailService.Message mess = new MailService.Message();
    	
		SimpleTimeZone zone = new SimpleTimeZone(0, "UTC");
    	
    	int satelliteCount = 0;
    	
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
        	
        	if (null == memcacheService) {
        		memcacheService = MemcacheServiceFactory.getMemcacheService();
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
    			
    			SatelliteElementSet cachedSatSet = (SatelliteElementSet)memcacheService.get(satSet.getCatalogNumber());
    			if (null == cachedSatSet) {
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
    				memcacheService.put(satSet.getCatalogNumber(), satSet);
    			} else {
    				msgBody += ("Satellite: " + satSet.getName() + " was in cache"+ NEW_LINE);
    				msgBody += ("Comparing satSet for: " + satSet.getName() + " with tle on SetNum: " + cachedSatSet.getSetNumber() + ", " + satSet.getSetNumber() + NEW_LINE);
    				if (satSet.getSetNumber().longValue() > cachedSatSet.getSetNumber().longValue()) {
	    				cachedSatSet.setName(satSet.getName());
	    				cachedSatSet.setLine1(satSet.getLine1());
	    				cachedSatSet.setLine2(satSet.getLine2());
	    				cachedSatSet.setSetNumber(satSet.getSetNumber());
	    				cachedSatSet.setUpdatedDate(Calendar.getInstance(zone).getTime());	
	    				satelliteElementSetDao.update(cachedSatSet.getCatalogNumber(), cachedSatSet);
	        			msgBody += ("Updated satellite: " + satSet.getName() + NEW_LINE);
	    				memcacheService.put(satSet.getCatalogNumber(), satSet);
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
    	
    	MailService mailServ = MailServiceFactory.getMailService();
    	
    	mess.setSender(BADGERJOHNSON_GMAIL_COM);
    	mess.setTo("david.johnson@blackpepper.co.uk");
    	mess.setSubject("loadKeps called");
    	mess.setTextBody(msgBody);
    
    	try {
			mailServ.send(mess);
		} catch (IOException e) {
        	LOGGER.severe(e.getMessage());
		}
        
		return Boolean.valueOf(success);
		
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
    public List<SatelliteElementSet> parseTLE(String urlString) throws IOException
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

	public TLE getTLE(Long catNum) {
		
		TLE tle = null;
		Transaction tx = null;
		
		try {
    	
			SatelliteElementSet satSet = (SatelliteElementSet)memcacheService.get(catNum);
			
			if (null == satSet) {
				
				tx = ds.beginTransaction();
		    	
				if (null == tx) {
					throw new NullPointerException("Could not create Transaction");
				}
				
				satSet = satelliteElementSetDao.findByPrimaryKey(catNum);
				
				if (null == satSet) {
	    			throw new NullPointerException("TLE was not found in cache or database");
				} else {
					memcacheService.put(catNum, satSet);
				}
				
				tx.commit();
			}
			
			String[] lines = new String[3];
			lines[0] = satSet.getName();
			lines[1] = satSet.getLine1();
			lines[2] = satSet.getLine2();
			
			tle = new TLE(lines);
			
			
		} catch (NullPointerException e) {
	        LOGGER.severe(e.getMessage());
		}	finally {
			if (null != tx && tx.isActive()) {
                tx.rollback();
            }
		}
		
		return tle;
	}

}
