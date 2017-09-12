package net.contal.spring.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import net.contal.spring.datahandler.DataMapper;
import net.contal.spring.datahandler.SqliteDbHandler;
import net.contal.spring.datahandler.SumCustomHandler;
import net.contal.spring.datahandler.TypeConvertor;
import net.contal.spring.model.CustomItem;
import net.contal.spring.model.DatesDto;
import net.contal.spring.model.GroupTotal;
import net.contal.spring.model.Settlement;
import net.contal.spring.model.TotalTerminalCard;



@Controller
public class MainController {

	  @GetMapping("/")
	public String index( Model model){  
		  model.addAttribute("datesDto",new DatesDto());
		 return "index";
	}
	
   @PostMapping("/loadDocket")
  public String loadDockets(@ModelAttribute DatesDto dateDto , Model model) {
	  
		List<CustomItem> items = new ArrayList<>();
		Connection conn = null;
		 Statement stmt  = null;
			try {
			  conn = SqliteDbHandler.openConnection();	 
				//Items 
				    stmt = conn.createStatement();
				  String     sql = CustomItem.getBetweenOrderByDate(dateDto.getFtomDate().getTime(),dateDto.getToDate().getTime());    
			   System.out.println(sql);
				  ResultSet  set =  stmt.executeQuery(sql);
				while(set.next()){
				CustomItem item = CustomItem.serializeItem(set);		
				items.add(item);
	
				    }
				stmt.close();
				conn.close();
				     }catch(Exception e){		
				    	 e.printStackTrace();
				}
			model.addAttribute("items",items);

	return "table";
    }
   
   
   
   private void getDataForThisModel(Date dateFrom ,Date dateTo) {
	   

		List<Date> days = TypeConvertor.listDaysForSettlement(dateFrom, dateTo);
		 
		ArrayList<CustomItem> itemsTime=new ArrayList<>();
		ArrayList<GroupTotal> groupTotals= new ArrayList<>();
		ArrayList<Settlement> setTotals = new ArrayList<>();
		try {
		Connection conn=SqliteDbHandler.openConnection();
			//Items 
			  Statement stmt = conn.createStatement();
			  String     sql =  CustomItem.selectBetweenDates(dateFrom, dateTo);
		      ResultSet  set =  stmt.executeQuery(sql);
			while(set.next()){
			CustomItem item = CustomItem.serializeItem(set);
			itemsTime.add(item);
			
			}
			
			
			Statement stmtSecoc = conn.createStatement();
			String sqlSt = Settlement.selectBetweenDates(dateFrom, dateTo);
			  ResultSet  setSecond=stmtSecoc.executeQuery(sqlSt);
			
				while(setSecond.next()){
					Settlement st = Settlement.serializeItem(setSecond);
					setTotals.add(st);
					
					}
			  
			//Total each cardType 
			String sqlTotals = CustomItem.groupTotalByCardType(dateFrom, dateTo);
			 ResultSet  setTotal =  stmt.executeQuery(sqlTotals);
		while(setTotal.next()){
				
				GroupTotal gt= new GroupTotal();
				String term=	set.getString("cardType");
				String gT =set.getString("total");
				gt.key=term;
				gt.total=Double.parseDouble(gT);
				groupTotals.add(gt);
			}

		 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
 
	     Double totalBySelecteddate = SumCustomHandler.getTotal(itemsTime);    
     
        Map<String,List<CustomItem>> mapTerminal = DataMapper.createTerminalGroup(itemsTime);
        List<TotalTerminalCard> terminalGroupTotals=SumCustomHandler.getTerminalCardTotal(mapTerminal);
        Double  total  =  terminalGroupTotals.stream().mapToDouble(t->t.getTotal()).sum();
 
//        request.setAttribute("terminaltotal", total);   
//        request.setAttribute("terminalgroups", terminalGroupTotals);
//        //set variables in request object 
//         request.setAttribute("settlement", setTotals);
//        request.setAttribute("total", totalBySelecteddate);
//        request.setAttribute("items", itemsTime);
//        request.setAttribute("cards",groupTotals);
        
	   
	   
   }
   


}
