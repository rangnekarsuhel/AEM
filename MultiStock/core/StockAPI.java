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
package com.aem.traning.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.ClientProtocolException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Model(adaptables = Resource.class)
public class StockAPI {
	Logger logger = LoggerFactory.getLogger(NseStockV2.class);

	private Map<String, String> formatedStock;
	private String propertiesSymbol;
	
	private String apiUrl = "https://query1.finance.yahoo.com/v7/finance/quote";
	public ArrayList<String> stockListArray = new ArrayList<String>();
	public String symbolLongName;

	
	public StockAPI(String propertiesSymbol) {
		super();
		
		try {
			// Create a method instance.
			logger.info("-----------------NseStockV2----------------------------");
			HttpClient client = new HttpClient();

			GetMethod get = new GetMethod(apiUrl);
			this.propertiesSymbol = propertiesSymbol + ".NS";
			get.setQueryString("symbols=" + this.propertiesSymbol);
			int status = client.executeMethod(get);
			if (status == 200) {
				String stockResult = get.getResponseBodyAsString();
				Map<String, String> map = getMapFromString(stockResult);
				String[] stockList = { "longName", "regularMarketPrice", "regularMarketPreviousClose", "regularMarketDayHigh", "regularMarketDayLow", "regularMarketTime" };
				
				for (String stk : stockList) {
					if (stk == "longName") {
						symbolLongName = map.get(stk);
					}
					else {
						stockListArray.add(map.get(stk));
					}
					
					
				}
				// Debug
				logger.info("Stock Status{}", status);
				// get.getQueryString();
				logger.info("Debug URI {} ", get.getURI());
				logger.info("Nsestring {}", map.get("longName"));
				logger.info("Nsestring {}", stockResult);

				for (String key : map.keySet()) {
					logger.info(key + " : " + map.get(key));
				}

				formatedStock = map;

			} else {
				formatedStock = null;
			}
		} catch (Exception e) {
			logger.error("Error while {} ", e.getMessage());
		}
		logger.info("******************NseStockV2***********************");
		
	}


	public String getSymbolLongName() {
		return symbolLongName;
	}

	/*
	 * Get the stock is Key/Value format.
	 */
	public Map<String, String> getFormatedtext() {
		return formatedStock;
	}

	/*
	 * Convert JSON string output to Key/Value list
	 */
	public static Map<String, String> getMapFromString(String s) {
		if (s == null || s.isEmpty()) {
			return new LinkedHashMap<>();
		}

		Map<String, String> map = new LinkedHashMap<>();

		String[] tokens = s.split(",");

		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].replace("\"", "");
			tokens[i] = tokens[i].replace("{", "");
			tokens[i] = tokens[i].replace("}", "");
			String[] subparts = tokens[i].split(":");

			if (subparts.length == 2) {
				map.put(subparts[0], subparts[1]);
			} else {
				map.put(subparts[0], "");

			}

		}

		return map;
	}

	/*
	 * Get the stock in array format.
	 */
	public ArrayList<String> getStockListArray() {
		return stockListArray;
	}

}