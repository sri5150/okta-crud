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
public class FindUserController {
	
	public static final String OKTA_DOMAIN = "https://dev-563587.okta.com";
	public static final String API_TOKEN = "00FZ8wJ6mWUVllRVZkQMIQpbYL9WMf1Ay1QlWF_v_r";

	private final Logger logger = LoggerFactory.getLogger(FindUserController.class);
			
    @GetMapping("/findUser")
    public String registrationForm(Model model) {
    	
    	logger.debug("/findUser - GET Request ");
    	
    	Registration defReg =  new Registration();
        model.addAttribute("registration", defReg);
        return "findUser";
    }

    @PostMapping("/findUser")
    public String registrationSubmit(@ModelAttribute Registration registration) {
    	
    	logger.debug("/findUser POST request(). Incoming reg={}", registration);
    	
    	
    	// Create user
    	Client client = Clients.builder()
    		    .setOrgUrl(OKTA_DOMAIN)
    		    .setClientCredentials(new TokenClientCredentials(API_TOKEN))
    		    .build();
    	
    	UserList users = client.listUsers(registration.getEmail(), null, null, null, null);
    	
    	try {
			User user = users.single();
			
			if(user != null) {    		
				String firstName = user.getProfile().getFirstName();
				String lastName = user.getProfile().getLastName();
				System.out.println("Found user: " + firstName + " " + lastName);
				registration.setFirstName(firstName);
				registration.setLastName(lastName);
				registration.setLogin(user.getProfile().getLogin());
			}
			
			logger.info("***Found user:{}", user);
		} catch (Exception e) {
			logger.error("Could not find unique user");
		}
    	
    	// Return to the Admin page
        return "findUser";
    }

}
