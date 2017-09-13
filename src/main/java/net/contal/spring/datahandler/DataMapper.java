package net.contal.spring.datahandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.SettlementDto;

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
	public static Map<String,List<CustomItemDto>> createMap(List<CustomItemDto> items){
		
		Map<String,List<CustomItemDto>> map=new HashMap<>();

		for(CustomItemDto item:items){
		
	    	if(!map.containsKey(item.getCardType())){	
	    		List<CustomItemDto> c = new ArrayList<>();
			c.add(item);
		    map.put(item.getCardType(),c);
		   }else{	
			List<CustomItemDto> c=map.get(item.getCardType());
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
	public static Map<String,List<CustomItemDto>> createTerminalGroup(List<CustomItemDto> items){		
		Map<String,List<CustomItemDto>> map=new HashMap<>();

		for(CustomItemDto item:items){
		
			if(!map.containsKey(item.getTerminalId())){	
				List<CustomItemDto> c=new ArrayList<>();
					c.add(item);
						map.put(item.getTerminalId(),c);
					}else{
			
						List<CustomItemDto> c = map.get(item.getTerminalId());
								c.add(item);
				}//else
			}	
		return map;	
	}
	

	
	/*
	 * Time comparison 
	 * 
	 */
	public static List<SettlementDto> afterPassedDateSettl(List<SettlementDto> list,Date minDate,Date maxDate){
		
		List<SettlementDto> itemsAll=new ArrayList<>();
			for (SettlementDto item : list) {
				if(item.date.after(minDate) && item.date.before(maxDate))
					itemsAll.add(item);	
						
		}
		return itemsAll;
	}
	
	
	/*
	 * Time comparison 
	 * 
	 */
	public static List<CustomItemDto> afterPassedDate(List<CustomItemDto> list,Date minDate,Date maxDate){
		
		List<CustomItemDto> itemsAll=new ArrayList<>();
			for (CustomItemDto customItem : list) {
				if(customItem.getDateTime().after(minDate)&& customItem.getDateTime().before(maxDate))
					itemsAll.add(customItem);	
						
		}
		return itemsAll;
	}
	

	
}
