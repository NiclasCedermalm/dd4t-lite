/*
 * Copyright 2014 Niclas Cedermalm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dd4tlite.freemarker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.factory.rawxml.RawXmlPageFactory;
import org.dd4tlite.model.Page;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Page Servlet
 *
 * @author nic
 */
public class PageServlet extends HttpServlet {

    // TODO: Have this in a seperate module for controllers?

    static private Log log = LogFactory.getLog(PageServlet.class);

    private ServletContext servletContext;
    private PageFactory pageFactory;
    private ViewProvider viewProvider = new FreemarkerViewProvider(); // Currently hardcoded, should be configurable

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.servletContext = config.getServletContext();


        String contentPath = "../content"; // config.getInitParameter("contentPath");   TODO: HAVE THIS CONFIGURABLE!!!!!!!
        log.info("Using content path: " + contentPath);

        //String viewProviderClassName = this.siteConfig.getProperty("")


        //int publicationId = Integer.parseInt(this.siteConfig.getProperty("publicationId"));
        //this.pageFactory = new SDLTridionBrokerPageFactory(publicationId);

        this.pageFactory = new RawXmlPageFactory(contentPath);

        // this.siteMetadata = new RawXmlSiteMetadata(contentPath + "/site-metadata.xml");   ?????
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    public void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Page page = this.pageFactory.getPage(request.getRequestURI());
            if ( page == null ) {
                log.error("Page '" + page + "' was not found");
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String redirectView = this.viewProvider.render(request, page);
            this.servletContext.getRequestDispatcher(redirectView).forward(request, response);

        }
        catch ( Exception e ) {
            // TODO: Improve the error handling here!!
            throw new ServletException("Error when rendering page: " + request.getRequestURI(), e);
        }


    }

}
