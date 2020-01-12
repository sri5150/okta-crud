package com.okta.spring.example.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import com.okta.spring.example.model.Registration;

@Controller
public class DeleteUserController {
	
	public static final String OKTA_DOMAIN = "https://dev-563587.okta.com";
	public static final String API_TOKEN = "00FZ8wJ6mWUVllRVZkQMIQpbYL9WMf1Ay1QlWF_v_r";
	
	private final Logger logger = LoggerFactory.getLogger(DeleteUserController.class);

    @GetMapping("/deleteUser")
    public String createForm(Model model) {
    	
    	logger.info("/deleteUser - GetMapping() ");
    	
    	 model.addAttribute("registration", new Registration());
        return "deleteUser";
    }

    @PostMapping("/deleteUser")
    public String registrationSubmit(@ModelAttribute Registration registration) {
    	
    	logger.info("In PostMapping(). Incoming reg={}", registration);
    	
    	
    	// Obtain client to find user
    	Client client = Clients.builder()
    		    .setOrgUrl(OKTA_DOMAIN)
    		    .setClientCredentials(new TokenClientCredentials(API_TOKEN))
    		    .build();
    	
    	UserList users = client.listUsers(registration.getEmail(), null, null, null, null);
    	try {
			User user = users.single();
			
			// If user exists, delete the user
			if(user != null) {       		
				user.deactivate();
				user.delete();
			}
			
			logger.info("***User was deleted {}", user);
		} catch (Exception e) {
			logger.error("Could not identify unique user");
		}    	
    	
    	// Return to the Admin page
        return "admin";
    }

}
