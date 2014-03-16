/**
 *  Copyright 2014 Niclas Cedermalm
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.simpleframework.xml.core;

import org.simpleframework.xml.Serializer;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * XmlUtils
 *
 * @author nic
 */
public class XmlUtils {

    static public Serializer createSerializer(Map initialSessionData) throws Exception {
        Persister serializer = new Persister();
        Map session = XmlUtils.openSession(serializer);
        if ( initialSessionData != null ) {
            session.putAll(initialSessionData);
        }
        return serializer;
    }

    static public Map openSession(Persister persister) throws Exception {
        return getSessionManager(persister).open().getMap();
    }

    static public void closeSession(Serializer serializer) throws Exception {
        if ( serializer instanceof Persister ) {
            getSessionManager((Persister) serializer).close();
        }
    }

    static private SessionManager getSessionManager(Persister persister) throws Exception {
        Field field = persister.getClass().getDeclaredField("manager");
        field.setAccessible(true);
        SessionManager sessionManager = (SessionManager) field.get(persister);
        return sessionManager;
    }
}
