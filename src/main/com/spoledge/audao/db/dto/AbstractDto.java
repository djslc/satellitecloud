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
package com.spoledge.audao.db.dto;


/**
 * This is the parent of all DTO classes.
 */
public abstract class AbstractDto implements java.io.Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a human readable string.
     */
    @Override
    public final String toString() {
        StringBuffer sb = new StringBuffer();

        contentToString( sb );

        // Class.getSimpleName() is not supported by GWT yet (v 1.6.4)
        // so we must compute it :
        String name = getClass().getName();
        name = name.substring( name.lastIndexOf( '.' ) + 1 );

        return name + '{' + sb.toString() + '}';
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected abstract void contentToString(StringBuffer sb);


    /**
     * Constructs the content for the toString() method.
     */
    protected final void append(StringBuffer sb, String name, Object val) {
        if (val != null) {
            if (sb.length() != 0) {
                sb.append( ", " );
            }

            sb.append( name ).append( '=' ).append( val );
        }
    }
}

