package net.contal.spring.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import net.contal.spring.dao.DocketDataDaoImpl;
import net.contal.spring.datahandler.DataMapper;
import net.contal.spring.datahandler.SumCustomHandler;
import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.DatesDto;
import net.contal.spring.dto.GroupTotalDto;
import net.contal.spring.dto.SettlementDto;
import net.contal.spring.dto.TotalTerminalCardDto;
import net.contal.spring.model.BankType;
import net.contal.spring.utils.TypeConvertor;



@Controller
public class MainController {

	
	@Autowired
	DocketDataDaoImpl daoCustom;
	
	  @GetMapping("/")
	public String index( Model model){  
		  BankType bankType =   daoCustom.loadBank("NAB");
		  daoCustom.getAllItems(bankType);
		  model.addAttribute("datesDto",new DatesDto());
		 return "index";
	}
	
   @PostMapping("/loadDocket")
  public String loadDockets(@ModelAttribute DatesDto dateDto , Model model) {
	  
		List<CustomItemDto> items = new ArrayList<>();
		model.addAttribute("items",items);

	return "table";
    }
   
   
   
   private void getDataForThisModel(Date dateFrom ,Date dateTo) {
	  
		List<Date> days = TypeConvertor.listDaysForSettlement(dateFrom, dateTo);	 
		ArrayList<CustomItemDto> itemsTime=new ArrayList<>();
		ArrayList<GroupTotalDto> groupTotals= new ArrayList<>();
		ArrayList<SettlementDto> setTotals = new ArrayList<>();	
	    Double totalBySelecteddate = SumCustomHandler.getTotal(itemsTime);    
        Map<String,List<CustomItemDto>> mapTerminal = DataMapper.createTerminalGroup(itemsTime);
        List<TotalTerminalCardDto> terminalGroupTotals=SumCustomHandler.getTerminalCardTotal(mapTerminal);
        Double  total  =  terminalGroupTotals.stream().mapToDouble(t->t.getTotal()).sum();

   }
   


}
