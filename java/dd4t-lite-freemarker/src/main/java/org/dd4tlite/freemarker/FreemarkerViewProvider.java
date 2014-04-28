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

import org.dd4tlite.model.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * Freemarker View Provider
 *
 * @author nic
 */
public class FreemarkerViewProvider implements ViewProvider {

    private FreemarkerRenderHelper renderHelper = new FreemarkerRenderHelper();

    @Override
    public String render(HttpServletRequest request, Page page) {

        request.setAttribute("dd4tPage", page);
        request.setAttribute("dd4t", this.renderHelper);
        request.setAttribute("template", new TemplateHelper());
        return "/page_templates/" + page.getPageTemplate().getViewName() + ".ftl";
    }
}
