package net.contal.spring.datahandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.contal.spring.model.CustomItem;
import net.contal.spring.model.Settlement;

/**
 * 
 * @author A.H.Safaie 
 *
 */
public class DataMapper {

	/*
	 * Create Map out of passed ArrayList 
	 * Key = CardType 
	 * 
	 * */
	public static Map<String,List<CustomItem>> createMap(List<CustomItem> items){
		
		Map<String,List<CustomItem>> map=new HashMap<>();

		for(CustomItem item:items){
		
	    	if(!map.containsKey(item.getCardType())){	
	    		List<CustomItem> c = new ArrayList<>();
			c.add(item);
		    map.put(item.getCardType(),c);
		   }else{	
			List<CustomItem> c=map.get(item.getCardType());
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
	public static Map<String,List<CustomItem>> createTerminalGroup(List<CustomItem> items){		
		Map<String,List<CustomItem>> map=new HashMap<>();

		for(CustomItem item:items){
		
			if(!map.containsKey(item.getTerminalId())){	
				List<CustomItem> c=new ArrayList<>();
					c.add(item);
						map.put(item.getTerminalId(),c);
					}else{
			
						List<CustomItem> c = map.get(item.getTerminalId());
								c.add(item);
				}//else
			}	
		return map;	
	}
	

	
	/*
	 * Time comparison 
	 * 
	 */
	public static List<Settlement> afterPassedDateSettl(List<Settlement> list,Date minDate,Date maxDate){
		
		List<Settlement> itemsAll=new ArrayList<>();
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
	public static List<CustomItem> afterPassedDate(List<CustomItem> list,Date minDate,Date maxDate){
		
		List<CustomItem> itemsAll=new ArrayList<>();
			for (CustomItem customItem : list) {
				if(customItem.getDateTime().after(minDate)&& customItem.getDateTime().before(maxDate))
					itemsAll.add(customItem);	
						
		}
		return itemsAll;
	}
	

	
}
