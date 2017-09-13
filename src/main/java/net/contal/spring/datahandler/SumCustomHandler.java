package net.contal.spring.datahandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.GroupTotalDto;
import net.contal.spring.dto.TotalTerminalCardDto;


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
	public static Float getTotal(Map<String,List<CustomItemDto>> map ){
		Float total=0.0f;
		//Map iterator values 
		for (List<CustomItemDto> items : map.values()) {
			//total all 
			for (CustomItemDto customItem : items) {	
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
	public static Double getTotal(List<CustomItemDto> list ){
		Double total=new Double(0);
		//Map iterator values 
		for (CustomItemDto item :list) {
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
	
	public static List<CustomItemDto>  mapToList(Map<String,List<CustomItemDto>> map ){
		List<CustomItemDto> itemsAll=new ArrayList<>();
		//Map iterator values 
		for (List<CustomItemDto> items : map.values()) {
			for (CustomItemDto customItem : items) 
				itemsAll.add(customItem);		
		}
		return itemsAll;
				
	}
	
	
	
	
	/*
	 * Calculate total for Map by key groups  
	 * Return array of type GroupTotal 
	 * */
	public static List<GroupTotalDto> getGroupTotal(Map<String,List<CustomItemDto>> map){
		
		
		List<GroupTotalDto> groupTotals=new ArrayList<>(); //pass as Attribute 
		//iterat with key 
		for(String key : map.keySet()) {
		 GroupTotalDto gp=new GroupTotalDto();
		 gp.key=key;
			List<CustomItemDto> keyValues=map.get(key);
			Double groupTotal=new Double(0);
			    for(CustomItemDto it:keyValues){
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
	public static List<TotalTerminalCardDto> getTerminalCardTotal(Map<String,List<CustomItemDto>> map){
		List<TotalTerminalCardDto> totalTerminalCard =  new ArrayList<>(); //pass as Attribute 
		Iterator<Entry<String, List<CustomItemDto>>> iter = map.entrySet().iterator();
	   while(iter.hasNext()) {
		     Entry<String, List<CustomItemDto>> entrySet = iter.next();
		     String key = entrySet.getKey();
			 TotalTerminalCardDto gp = new TotalTerminalCardDto();
		     gp.terminalId = key;
			 List<CustomItemDto> keyValues = entrySet.getValue();
			//totals
			Double groupTotal = Double.valueOf(0);
			Double    mvTotal = Double.valueOf(0);
			Double otherTotal = Double.valueOf(0);
			  for(CustomItemDto it:keyValues){	    	
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
