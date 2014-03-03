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
