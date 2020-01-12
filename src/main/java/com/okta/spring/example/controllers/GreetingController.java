package com.okta.spring.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.okta.spring.example.model.Greeting;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
    	
    	System.out.println("GetMapping() ");
    	
    	
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
    	
    	System.out.println("In PostMapping(). id=" + greeting.getId() + ", value=" + greeting.getContent());
    	
        return "result";
    }

}
