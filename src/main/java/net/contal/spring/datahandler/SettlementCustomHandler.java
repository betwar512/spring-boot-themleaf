package net.contal.spring.datahandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import net.contal.spring.dto.SettlementDto;
import net.contal.spring.utils.TypeConvertor;

/*
 * Use LogCustomHelper property for passing the String array to settlements
 * Tuned for westpack log files 
 * V 2.0
 * */
public class SettlementCustomHandler {
	private static final Logger logger = Logger.getLogger(SettlementCustomHandler.class);
	  private SettlementCustomHandler() {
		    throw new IllegalStateException("Utility class");
		  }

 public static List<SettlementDto>  getWestpacSettlements(List<List<String>> retMap){

	List<SettlementDto> list=new ArrayList<>();
	for (List<String> t : retMap){

	   	boolean settlement  =false;
		boolean terminalBool=false;
		SettlementDto settl=new SettlementDto();
		 settl.purchAmount=0f;
		for (String r : t) {//String inside Array String x
			String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
			//cleanUp array 
			List<String> cleanSplit=new ArrayList<>(Arrays.asList(stSplit));
		 cleanSplit.removeAll(Arrays.asList(""," ",null));
			String stS=cleanSplit.get(0);
		    if(r.contains("PRE-SETTLEMENT")){
					settlement=true;
	    }    
			if(terminalBool){ //terminal code comes 1 iteration after Terminal keyWord 	
				settl.terminalId=stSplit[0];
				terminalBool=false;
			        }

			
				if(settlement){
					logger.info(r);
						if(r.contains("MERCHANT ID"))
							settl.merchantId=cleanSplit.get(2);	     
						else
							if(r.contains("TERMINAL ID")) 
								settl.terminalId=cleanSplit.get(2);
							else
								if(stS.contains("DATE/TIME")){
										String month="";
										String date="";
										String day=cleanSplit.get(1).substring(0, 2);
										StringBuilder stBuilder = new StringBuilder();
									if(cleanSplit.get(1).length()<5){ //FEB issue 				 
										  month = cleanSplit.get(1).substring(2, 4);
										stBuilder.append(month+"B"+ " " + day + " " + "2016" + " 00:00");
										}else{ 	
											stBuilder.append(cleanSplit.get(1).substring(2,5) + " " + 
													day + " " + " 20"+ cleanSplit.get(1).substring(5, 7) + 
												" " + cleanSplit.get(2))	;
									     }							 
                               logger.info(stBuilder.toString());
                               if(date== "") {
                            	   logger.debug("Empty date ");
                            	   }
								Date d = TypeConvertor.convertStringToDateElixer(stBuilder.toString());
								settl.date=d;
									}else
										if(r.contains("TOTAL AUD")) {
											 String strTemp = cleanSplit.get(cleanSplit.size()-1).replace("$"," ").trim();			
										  settl.total = Double.valueOf(strTemp);			
										}  else
					 							if(r.contains("TOTAL PUR CNT")) {
					 								settl.purchCount=cleanSplit.get(3);
					 								}

								}
				

				
                 }
		
		  if(settl.isValid()) {
					list.add(settl);
				}
	   //   settlement = false;
	 }
		return list;
   }	
}
