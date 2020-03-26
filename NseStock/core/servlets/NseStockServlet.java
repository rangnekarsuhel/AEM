/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.traning.core.servlets;

import com.aem.traning.core.models.NseStock;
import com.aem.traning.core.models.NseStockV2;
import com.aem.traning.core.api.*;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 * "sling.servlet.extensions=" + "txt"
 * "sling.servlet.methods=" + HttpConstants.METHOD_GET,
 *                    //"sling.servlet.selectors="+ "sample",
 *                    "sling.servlet.resourceTypes="+ "AEMMaven22/components/structure/page",
 */
@Component(service=Servlet.class,
           property={
        		   "sling.servlet.resourceTypes="+ "AEMMaven22/components/structure/page",
        		   "sling.servlet.selectors="+ "nse",
           })
@ServiceDescription("Simple Demo Servlet")
public class NseStockServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
        resp.setContentType("text/plain");
        

        // Version 1 API Call
        /*NseStock nsestock = resource.adaptTo(NseStock.class);
        String output = nsestock.getFormatedtext();
        resp.getWriter().write("Symbol = " + nsestock.getPropertiesSymbol());
        resp.getWriter().write(output);*/
        
        // Version 2 API Call
       NseStockV2 nsestockV2 = resource.adaptTo(NseStockV2.class);
        Map<String, String> map = nsestockV2.getFormatedtext();
        ArrayList<String> stockListArray = nsestockV2.getStockListArray();
        String stockName = nsestockV2.getSymbolLongName();
        /*String[] stockList = {"longName", "regularMarketPrice", "regularMarketDayHigh", "regularMarketDayLow", "regularMarketPreviousClose", "regularMarketTime"};        
        for (String stk : stockList) {
        	resp.getWriter().write(stk + " : " + map.get(stk));
        }*/
        // Get ALL data
        resp.getWriter().write(map.toString());
        //resp.getWriter().write(stockName);
        //resp.getWriter().write(stockListArray.toString());
        
        /*for (String key : map.keySet()) {
        	resp.getWriter().write(", '" + key + "' : " + map.get(key));
        }*/
        
        
    }
}
