package net.contal.spring.datahandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.contal.spring.model.*;


/**
 * 
 * @author A.H.Safaie 
 *
 */
public abstract class SumCustomHandler {

	    private SumCustomHandler() {
		   throw new IllegalStateException("Utility class");
		  }
	
	/*
	 * Coagulate total for the map 
	 * */
	public static Float getTotal(Map<String,List<CustomItem>> map ){
		Float total=0.0f;
		//Map iterator values 
		for (List<CustomItem> items : map.values()) {
			//total all 
			for (CustomItem customItem : items) {	
				if(customItem.getTotalAmount()!=null) {
	                    total+=customItem.getTotalAmount();	
	                 }		
			      }
		       }
		
		return total;
		
	}
	/*
	 * GetTotal by ArrayList 
	 * 
	 * */
	public static Double getTotal(List<CustomItem> list ){
		Double total=new Double(0);
		//Map iterator values 
		for (CustomItem item :list) {
			//total all 
				if(item.getTotalAmount()!=null){
					if(item.getStatus().contains("APPROVED")) {
	                     total+=item.getTotalAmount();	
	                    }
	                 }		
		}
		return total;
		
	}
	
	
	
	
	/*
	 * Convert mapped items to ArrayList 
	 */
	
	public static List<CustomItem>  mapToList(Map<String,List<CustomItem>> map ){
		List<CustomItem> itemsAll=new ArrayList<>();
		//Map iterator values 
		for (List<CustomItem> items : map.values()) {
			for (CustomItem customItem : items) 
				itemsAll.add(customItem);		
		}
		return itemsAll;
				
	}
	
	
	
	
	/*
	 * Calculate total for Map by key groups  
	 * Return array of type GroupTotal 
	 * */
	public static List<GroupTotal> getGroupTotal(Map<String,List<CustomItem>> map){
		
		
		List<GroupTotal> groupTotals=new ArrayList<>(); //pass as Attribute 
		//iterat with key 
		for(String key : map.keySet()) {
		 GroupTotal gp=new GroupTotal();
		 gp.key=key;
			List<CustomItem> keyValues=map.get(key);
			Double groupTotal=new Double(0);
			    for(CustomItem it:keyValues){
			    	if(it.getStatus().contains("APPROVED"))
			    groupTotal+=it.getTotalAmount();		    	
			    }
			gp.total=groupTotal; //get total 
				groupTotals.add(gp);
		}
		
		return groupTotals;
	}
	
	
	
	
	/*
	 * Calculate total for Map by key => TerminlaId 
	 * CardType Total
	 * OutPut: Map Class: TotalTerminalCard
	 * Return array of type GroupTotal 
	 * */
	public static List<TotalTerminalCard> getTerminalCardTotal(Map<String,List<CustomItem>> map){
		List<TotalTerminalCard> totalTerminalCard =  new ArrayList<>(); //pass as Attribute 
		Iterator<Entry<String, List<CustomItem>>> iter = map.entrySet().iterator();
	   while(iter.hasNext()) {
		     Entry<String, List<CustomItem>> entrySet = iter.next();
		     String key = entrySet.getKey();
			 TotalTerminalCard gp = new TotalTerminalCard();
		     gp.terminalId = key;
			 List<CustomItem> keyValues = entrySet.getValue();
			//totals
			Double groupTotal = Double.valueOf(0);
			Double    mvTotal = Double.valueOf(0);
			Double otherTotal = Double.valueOf(0);
			  for(CustomItem it:keyValues){	    	
			    	if(it.getStatus().contains("APPROVED")){
			    		groupTotal+=it.getTotalAmount();	
				    
				    if(it.getCardType().contains("MASTERCARD")|| it.getCardType().contains("VISA")) {
				       	mvTotal += it.getTotalAmount();
				        }else {
				      	otherTotal+=it.getTotalAmount();
				      	} 
				      }
			   }
 
			gp.total = groupTotal; //get total 
			gp.totalMasterVisa = mvTotal;
			gp.totalOthers = otherTotal;
			totalTerminalCard.add(gp);
		}
		
		return totalTerminalCard;
	}
	

	
}
