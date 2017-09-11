package net.contal.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

	  @GetMapping("/")
	public String index( Model model){
		return "index";
	}
	
//	   @RequestMapping("/hello")
//	    public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
//	        model.addAttribute("name", name);
//	        return "hello";
//	    }


}
