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
package org.dd4tlite.sdltridion.factory;

import com.tridion.data.*;
import com.tridion.storage.PageMeta;
import com.tridion.storage.StorageManagerFactory;
import com.tridion.storage.StorageTypeMapping;
import com.tridion.storage.dao.ItemDAO;
import com.tridion.storage.dao.PageDAO;
import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.model.Page;
import org.dd4tlite.model.impl.xml.XmlPage;
import org.dd4tlite.sdltridion.service.SDLTridionLinkResolver;
import org.dd4tlite.service.LinkResolver;
import org.dd4tlite.util.IOUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * SDL Tridion Broker Page Factory
 *
 * @author nic
 */
public class SDLTridionBrokerPageFactory implements PageFactory {

    private int publicationId;

    private Map<Class<?>, Object> dependencies = new HashMap<>();

    static private Map<Class<?>, Object> DEFAULT_DEPENDENCIES = new HashMap<>();

    static {
        DEFAULT_DEPENDENCIES.put(LinkResolver.class, new SDLTridionLinkResolver());
    }

    public SDLTridionBrokerPageFactory(int publicationId) {
        this.publicationId = publicationId;
        this.dependencies.putAll(DEFAULT_DEPENDENCIES);
    }

    public SDLTridionBrokerPageFactory(int publicationId, Map<Class<?>, Object> dependencies) {
        this(publicationId);
        this.dependencies.putAll(dependencies);
    }

    @Override
    public Page getPage(String url) throws Exception {

        ItemDAO itemDAO = (ItemDAO) StorageManagerFactory.getDAO(this.publicationId, StorageTypeMapping.PAGE_META);
        PageMeta pageMeta = itemDAO.findByPageURL(this.publicationId, url);
        if ( pageMeta == null ) {
            return null;
        }
        PageDAO pageDAO = (PageDAO) StorageManagerFactory.getDAO(pageMeta.getPublicationId(), StorageTypeMapping.PAGE);
        CharacterData data = pageDAO.findByPrimaryKey(this.publicationId, pageMeta.getItemId());
        String xmlContent = IOUtils.convertStreamToString(data.getInputStream());

        File xmlFile = new File(url);
        String path = xmlFile.getParent();
        String filename = xmlFile.getName();

        return XmlPage.load(path, filename, xmlContent, dependencies);
    }
}
