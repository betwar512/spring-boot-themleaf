package net.contal.spring.datahandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.contal.spring.model.Settlement;

/*
 * Use LogCustomHelper property for passing the String array to settlements
 * Tuned for westpack log files 
 * V 2.0
 * */
public class SettlementCustomHandler {


	public static ArrayList<Settlement>  getWestpacSettlements(ArrayList<ArrayList<String>> retMap){
		
		
		//SettlementWriter.writeToFile(retMap);
		
		ArrayList<Settlement> list=new ArrayList<>();
		for (ArrayList<String> t : retMap){

	   	boolean settlement  =false;
		boolean terminalBool=false;

		 Settlement settl=new Settlement();
		
		 settl.purchAmount=0f;
		 int index=0; 
		for (String r : t) {//String inside Array String x
			String[] stSplit=r.split(" "); //split String by space to detriment what's in our String  
			//cleanUp array 
			List<String> cleanSplit=new ArrayList<String>(Arrays.asList(stSplit));
		 cleanSplit.removeAll(Arrays.asList(""," ",null));
			String stS=cleanSplit.get(0);
		  //  preSettlementCounter++;
		    if(r.contains("PRE-SETTLEMENT")){
					settlement=true;
	    }    
			if(terminalBool){ //terminal code comes 1 iteration after Terminal keyWord 	
				settl.terminalId=stSplit[0];
				terminalBool=false;
			        }

			
				if(settlement){
					System.out.println(r);
						if(r.contains("MERCHANT ID"))
							settl.merchantId=cleanSplit.get(2);	     
						else
							if(r.contains("TERMINAL ID")) 
								settl.terminalId=cleanSplit.get(2);
							else
								if(stS.contains("DATE/TIME")){
//									String date=cleanSplit.get(1) + cleanSplit.get(2);
//									settl.date=TypeConvertor.settelmentDate(date);
									
									
									
										String month="";
										String year="";
										String date="";
										String day=cleanSplit.get(1).substring(0, 2);
										if(cleanSplit.get(1).length()<5){ //FEB issue 
									//	System.out.println("Less Time date");
										month=cleanSplit.get(1).substring(2, 4);
										
										
								//		String yearAfter = t.get(index+1);
										
										//String[] spList = yearAfter.split(" ");
										 year ="2016";//+ spList[0]; 
										month+="B";
					
										date= month +" "+day+" "+year+" 00:00" ; //+" " +spList[1];
										
										}else{ 
										//TODO problem here need to be changed 
										 month=cleanSplit.get(1).substring(2,5);
										 year =" 20"+ cleanSplit.get(1).substring(5, 7);
										date	= month +" "+day+" "+year +" "+cleanSplit.get(2);
										
										 }							 
                               System.out.println(date);
                               if(date== "")
                            	   System.out.println("Empty date ");
									 Date d = TypeConvertor.convertStringToDateElixer(date);
									 settl.date=d;
									}
									else
										if(r.contains("NET TOTAL AUD"))
											settl.purchAmount=Float.valueOf(cleanSplit.get(3).substring(1));			
					 						else
					 							if(r.contains("TOTAL PUR CNT"))settl.purchCount=cleanSplit.get(3);
								index++;	}
				
								
//			if(!r.contains("-----------")){	
//				try (Writer writer = new BufferedWriter(new FileWriter("myfile.txt", true))) {
//			   writer.append(r);
//			    } catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTra0
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
				
       }
		
										if(settl.isValid())
													list.add(settl);
															settlement=false;
		
	 }
		return list;
   }	
}
