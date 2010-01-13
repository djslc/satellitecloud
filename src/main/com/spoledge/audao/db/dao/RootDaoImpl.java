/*
 * Copyright 2009 Spolecne s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spoledge.audao.db.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is the root parent of all DAO implementation classes.
 * It uses all common generic methods and utilities.
 * The implementation is not thread safe - we assume
 * that the client creates one DAO impl per thread.
 */
public abstract class RootDaoImpl {

    /**
     * The logger.
     */
    protected Log log = LogFactory.getLog( getClass());


    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new DAO implementation.
     */
    protected RootDaoImpl() {
    }


    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the table name.
     */
    public abstract String getTableName();


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////

    protected final void checkNull( String name, Object value) throws DaoException {
        if ( value == null ) {
            throw new DaoException("Value of column '" + name + "' cannot be null");
        }
    }


    protected final void checkMaxLength( String name, String value, int maxLength)
            throws DaoException {

        if ( value.length() > maxLength ) {
            throw new DaoException("Value of column '" + name + "' cannot have more than " + maxLength + " chars");
        }
    }


    protected final void checkLength( String name, String value, int minLength, int maxLength)
            throws DaoException {

        checkMaxLength( name, value, maxLength);

        if ( value.length() < minLength ) {
            throw new DaoException("Value of column '" + name + "' must have at least " + minLength + " chars");
        }
    }


    protected final int pageOffset( int pageNumber, int pageSize ) {
        return (pageNumber - 1) * pageSize;
    }


    protected final void debugSql( String sql ) {
        if (log.isDebugEnabled()) {
            log.debug( sqlLog( sql, null));
        }
    }


    protected final void debugSql( String sql, Object param ) {
        if (log.isDebugEnabled()) {
            log.debug( sqlLog( sql, null) + " PARAM: " + param );
        }
    }


    protected final void debugSql( String sql, Object[] params ) {
        if (log.isDebugEnabled()) {
            log.debug( sqlLog( sql, params));
        }
    }


    protected final void errorSql( Throwable t, String sql ) {
        log.error( sqlLog( sql, null), t );
    }


    protected final void errorSql( Throwable t, String sql, Object param ) {
        log.error( sqlLog( sql, null) + " PARAM: " + param, t );
    }


    protected final void errorSql( Throwable t, String sql, Object[] params ) {
        log.error( sqlLog( sql, params), t );
    }


    /**
     * Used for logging.
     */
    protected final String sqlLog( String sql, Object[] params) {
        String ret = "SQL: " + sql;

        if (params == null || params.length == 0) {
            return ret;
        }
        else {
            StringBuffer sb = new StringBuffer();

            for (Object param : params) {
                if (param != null) {
                    if (sb.length() != 0) {
                        sb.append( ", " );
                    }

                    if ( param instanceof Number ) {
                        sb.append( param );
                    }
                    else {
                        sb.append('\'').append( param ).append( '\'' );
                    }
                }
            }

            return  ret + "; PARAMS: " + sb;
        }
    }

}

