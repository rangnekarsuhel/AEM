package com.aem.traning.core.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
   
import com.adobe.cq.sightly.WCMUsePojo;
 
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.NodeIterator; 
 
public class MultifieldProcess extends WCMUsePojo{
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<MultifiledStockAttribute> submenuItems = new ArrayList<MultifiledStockAttribute>();
	
	private StockAPI StockAPI;
	public ArrayList<String> stockListArray = new ArrayList<String>();
	private String symbolLongName;
	
	@Override
	public void activate() throws Exception {
	 
	    Node currentNode = getResource().adaptTo(Node.class);
	    NodeIterator ni =  currentNode.getNodes() ; 
	     
	    //get the grandchild node of the currentNode - which represents where the MultiField values are stored
	    while (ni.hasNext()) {
	         
	        Node child = (Node)ni.nextNode(); 
	         
	        NodeIterator ni2 =  child.getNodes() ; 
	        setMultiFieldItems(ni2); 
	    }
	}
	
	/**
	* Method to get Multi field data
	* @return submenuItems
	*/
	private List<MultifiledStockAttribute> setMultiFieldItems(NodeIterator ni2) {
	   
		try{
		    
		    String stockSymbol;
		    String longName;
		    String regularMarketPrice;
		    String regularMarketDayHigh;
		    String regularMarketDayLow;
		    String regularMarketPreviousClose;
		    String regularMarketTime;
		 
		    //THIS WILL READ THE VALUE OF THE CORAL3 Multifield and set them in a collection 
		    while (ni2.hasNext()) {
		         
		    	MultifiledStockAttribute menuItem = new MultifiledStockAttribute();
		         
		         
		        Node grandChild = (Node)ni2.nextNode();
		         
		        log.info("*** GRAND CHILD NODE PATH IS "+grandChild.getPath());
		         
		        stockSymbol = grandChild.getProperty("stockSymbol").getString();
		        
		        StockAPI = new StockAPI(stockSymbol);
		        Map<String, String> map  = StockAPI.getFormatedtext();
		        stockListArray = StockAPI.stockListArray;
		        symbolLongName = StockAPI.symbolLongName;
				
		        //String[] stockList = {"longName", "regularMarketPrice", "regularMarketDayHigh", "regularMarketDayLow", "regularMarketPreviousClose", "regularMarketTime"};        
		        longName = map.get("longName");
		        regularMarketPrice = map.get("regularMarketPrice");
		        regularMarketDayHigh = map.get("regularMarketDayHigh");
		        regularMarketDayLow = map.get("regularMarketDayLow");
		        regularMarketPreviousClose = map.get("regularMarketPreviousClose");
		        regularMarketTime = map.get("regularMarketTime");
		        
		        
		        log.info("*** MarketPrice "+regularMarketPrice);
		         
		        menuItem.setStockSymbol(stockSymbol);
		        menuItem.setLongName(longName);
		        menuItem.setRegularMarketPrice(regularMarketPrice);
		        menuItem.setRegularMarketDayHigh(regularMarketDayHigh); 
		        menuItem.setRegularMarketDayLow(regularMarketDayLow);
		        menuItem.setRegularMarketPreviousClose(regularMarketPreviousClose);
		        menuItem.setRegularMarketTime(regularMarketTime);
		        submenuItems.add(menuItem);
		    }
		}
		   
		catch(Exception e){
		    log.error("Exception while Multifield data {}", e.getMessage(), e);
		}
		return submenuItems;
	}
 
	public List<MultifiledStockAttribute> getMultiFieldItems() {
		return submenuItems;
	}

	public ArrayList<String> getStockListArray() {
		return stockListArray;
	}

	public String getSymbolLongName() {
		return symbolLongName;
	}
	

}