package net.contal.spring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import net.contal.spring.dao.DocketDataDaoImpl;
import net.contal.spring.dto.CustomItemDto;
import net.contal.spring.dto.DatesDto;
import net.contal.spring.model.BankType;


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
   

}
