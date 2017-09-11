package net.contal.spring.datahandler;
import java.util.ArrayList;
import java.util.HashMap;

import net.contal.spring.model.*;


/*
 * 
 * */
public class SumCustomHandler {

	
	/*
	 * Coagulate total for the map 
	 * */
	public static Float getTotal(HashMap<String,ArrayList<CustomItem>> map ){
		
	
		Float total=0.0f;
		//Map iterator values 
		for (ArrayList<CustomItem> items : map.values()) {
			//total all 
			for (CustomItem customItem : items) {	
		
				if(customItem.getTotalAmount()!=null)
	                 total+=customItem.getTotalAmount();			
			}
			
		}
		return total;
		
	}
	/*
	 * GetTotal by ArrayList 
	 * 
	 * */
	public static Double getTotal(ArrayList<CustomItem> list ){
		
		
		Double total=new Double(0);
		//Map iterator values 
		for (CustomItem item :list) {
			//total all 
				if(item.getTotalAmount()!=null){
					if(item.getStatus().contains("APPROVED"))
	                 total+=item.getTotalAmount();	
	                 }		
		}
		return total;
		
	}
	
	
	
	
	/*
	 * Convert mapped items to ArrayList 
	 */
	
	public static ArrayList<CustomItem>  mapToList(HashMap<String,ArrayList<CustomItem>> map ){
		
		ArrayList<CustomItem> itemsAll=new ArrayList<>();
		//Map iterator values 
		for (ArrayList<CustomItem> items : map.values()) {
			for (CustomItem customItem : items) 
				itemsAll.add(customItem);		
		}
		return itemsAll;
				
	}
	
	
	
	
	/*
	 * Calculate total for Map by key groups  
	 * Return array of type GroupTotal 
	 * */
	public static ArrayList<GroupTotal> getGroupTotal(HashMap<String,ArrayList<CustomItem>> map){
		
		
		ArrayList<GroupTotal> groupTotals=new ArrayList<>(); //pass as Attribute 
		//iterat with key 
		for (String key : map.keySet()) {
		 GroupTotal gp=new GroupTotal();
		 gp.key=key;
			ArrayList<CustomItem> keyValues=map.get(key);
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
	public static ArrayList<TotalTerminalCard> getTerminalCardTotal(HashMap<String,ArrayList<CustomItem>> map){
		
		
		ArrayList<TotalTerminalCard> TotalTerminalCard=new ArrayList<>(); //pass as Attribute 
		//iterat with key 
		for (String key : map.keySet()) {
			TotalTerminalCard gp=new TotalTerminalCard();
		 gp.terminalId=key;
		 
			ArrayList<CustomItem> keyValues=map.get(key);
			//totals
			Double groupTotal=new Double(0);
			Double mvTotal=new Double(0);
			Double otherTotal=new Double(0);

			    for(CustomItem it:keyValues){
			    	
			    	if(it.getStatus().contains("APPROVED")){
			    	
			    groupTotal+=it.getTotalAmount();	
			    
			    if(it.getCardType().contains("MASTERCARD")|| it.getCardType().contains("VISA"))
			    	mvTotal+= it.getTotalAmount();
			    else 
			    	otherTotal+=it.getTotalAmount();
			    }
			   }
			    //set Item
			    
			gp.total= groupTotal; //get total 
			gp.totalMasterVisa= mvTotal;
			gp.totalOthers= otherTotal;
			TotalTerminalCard.add(gp);
		}
		
		return TotalTerminalCard;
	}
	

	
}
