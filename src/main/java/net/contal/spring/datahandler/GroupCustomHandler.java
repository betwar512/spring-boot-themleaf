package net.contal.spring.datahandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.contal.spring.model.CustomItem;
import net.contal.spring.model.Settlement;

public class GroupCustomHandler {


	
	/*
	 * Create Map out of passed ArrayList 
	 * Key = CardType 
	 * 
	 * */
	public static HashMap<String,ArrayList<CustomItem>> createMap(ArrayList<CustomItem> items){
		
		HashMap<String,ArrayList<CustomItem>> map=new HashMap<>();
		
		
		for(CustomItem item:items){
		
		if(!map.containsKey(item.getCardType())){	
			ArrayList<CustomItem> c=new ArrayList<>();
			c.add(item);
		map.put(item.getCardType(),c);
		}else{
			
		ArrayList<CustomItem> c=map.get(item.getCardType());
		c.add(item);
			
				}//else
			}
		return map;
		}
	
	/*
	 * Grouping method by TerminalId 
	 * Map take place by using card type as a Key 
	 * 
	 * */
	public static HashMap<String,ArrayList<CustomItem>> createTerminalGroup(ArrayList<CustomItem> items){
		
				HashMap<String,ArrayList<CustomItem>> map=new HashMap<>();
		
		
		for(CustomItem item:items){
		
			if(!map.containsKey(item.getTerminalId())){	
				ArrayList<CustomItem> c=new ArrayList<>();
					c.add(item);
						map.put(item.getTerminalId(),c);
					}else{
			
						ArrayList<CustomItem> c = map.get(item.getTerminalId());
								c.add(item);
				}//else
			}	
		return map;	
	}
	

	
	/*
	 * Time comparison 
	 * 
	 */
	public static ArrayList<Settlement> afterPassedDateSettl(ArrayList<Settlement> list,Date minDate,Date maxDate){
		
		ArrayList<Settlement> itemsAll=new ArrayList<Settlement>();
			for (Settlement item : list) {
				if(item.date.after(minDate) && item.date.before(maxDate))
					itemsAll.add(item);	
						
		}
		return itemsAll;
	}
	
	
	/*
	 * Time comparison 
	 * 
	 */
	public static ArrayList<CustomItem> afterPassedDate(ArrayList<CustomItem> list,Date minDate,Date maxDate){
		
		ArrayList<CustomItem> itemsAll=new ArrayList<>();
			for (CustomItem customItem : list) {
				if(customItem.getDateTime().after(minDate)&& customItem.getDateTime().before(maxDate))
					itemsAll.add(customItem);	
						
		}
		return itemsAll;
	}
	

	
}
