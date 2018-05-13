/*
 * Copyright 2013 Integrity Ltd.
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
 * 
 * @author Zoltan Papp
 * 
 */
package hu.infokristaly.homework.pfext.middle.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * The RequestUtils class.
 */
public class RequestUtils {
    
    /**
     * Gets the context path.
     *
     * @return the context path
     */
    public static String getContextPath() {
        String serverRootPath = "";
        String contextPath = "";
        Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request instanceof HttpServletRequest) {
            String requestURL = ((HttpServletRequest) request).getRequestURL().toString();
            String requestURI = ((HttpServletRequest) request).getRequestURI();
            contextPath = ((HttpServletRequest) request).getContextPath();
            serverRootPath = requestURL.substring(0, requestURL.indexOf(requestURI));
        }
        return serverRootPath + contextPath;
    }

    /**
     * Resolve remote address.
     *
     * @return the string
     */
    public static String resolveRemoteAddress() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
    }
    
    /**
     * Resolve session id.
     *
     * @return the string
     */
    public static String resolveSessionId() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession().getId();
    }

}
