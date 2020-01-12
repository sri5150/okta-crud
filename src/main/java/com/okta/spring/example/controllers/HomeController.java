package com.okta.spring.example.controllers;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import com.okta.spring.example.model.Registration;

@Controller
public class HomeController {
	
	public static final String OKTA_DOMAIN = "https://dev-563587.okta.com";
	public static final String API_TOKEN = "00FZ8wJ6mWUVllRVZkQMIQpbYL9WMf1Ay1QlWF_v_r";
	
	public static final String DEF_ADMIN = "tls1.rock@gmail.com";
	
	private final Logger logger = LoggerFactory.getLogger(HomeController.class);
			
			
    @GetMapping("/")
    public ModelAndView home(Model model) {
    	
    	logger.info("/ - GetMapping() ");
    	
    	Registration registration = new Registration();
    	
     	// Obtain client to find user
     	Client client = Clients.builder()
     		    .setOrgUrl(OKTA_DOMAIN)
     		    .setClientCredentials(new TokenClientCredentials(API_TOKEN))
     		    .build();
     	
     	UserList users = client.listUsers(DEF_ADMIN, null, null, null, null);
     	
     	try {
 	    	User user = users.single();
 	    	
 	    	if(user != null) {
 	    		
 	    		String lastLogindt = user.getProfile().get("lastLogindt").toString();
 	    		int loginCount = Integer.parseInt((user.getProfile().get("loginCount").toString()));
 	    		
 	    		// Update model
 	    		registration.setLastLogin(lastLogindt);
 	    		registration.setLoginCount(loginCount);
 	    		
 	    		logger.info("lastLogin = {}, count = {}", registration.getLastLogin(), registration.getLoginCount());
 	    		
 	    		// Update profile
 	    		String now = LocalDateTime.now().toString();
 	    		user.getProfile().put("lastLogindt", now);
 	    		user.getProfile().put("loginCount", ++loginCount);
 	    		user.update();
 	    		
 	    	}
 	    	
 	    	logger.info("***User was updated: {}", user);
     	} catch (Exception e) {
     		logger.error("Unique user was not found");
 		}
     	
     	registration.setFirstName("zsdbx");
     	
     	ModelAndView modelView = new ModelAndView("home");
     	modelView.addObject("registration", registration);
     			
     	
        return modelView;
    }
}