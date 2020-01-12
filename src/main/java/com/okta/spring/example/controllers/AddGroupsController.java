package com.okta.spring.example.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.okta.sdk.resource.group.Group;
import com.okta.sdk.resource.group.GroupList;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserList;
import com.okta.spring.example.model.Registration;

@Controller
public class AddGroupsController {
	
	public static final String OKTA_DOMAIN = "https://dev-563587.okta.com";
	public static final String API_TOKEN = "00FZ8wJ6mWUVllRVZkQMIQpbYL9WMf1Ay1QlWF_v_r";
	
	public static final String ADMINS_GROUP = "admins";
	
	private final Logger logger = LoggerFactory.getLogger(AddGroupsController.class);

    @GetMapping("/makeAdmin")
    public String registrationForm(Model model) {
    	
    	logger.info("/makeAdmin - GET Request ");
    	
    	Registration defReg =  new Registration();
        model.addAttribute("registration", defReg);
        return "makeAdmin";
    }

    @PostMapping("/makeAdmin")
    public String registrationSubmit(@ModelAttribute Registration registration) {
    	
    	logger.info("/makeAdmin POST request(). Incoming reg={}", registration);
    	
    	// Create user
    	Client client = Clients.builder()
    		    .setOrgUrl(OKTA_DOMAIN)
    		    .setClientCredentials(new TokenClientCredentials(API_TOKEN))
    		    .build();
    	
    	
    	
    	UserList users = client.listUsers(registration.getEmail(), null, null, null, null);
    	User user = users.single();
    	
    	GroupList groupList = client.listGroups();
    	String adminGroupId = getGroupId(groupList, ADMINS_GROUP);
    	
    	if(user != null && adminGroupId != null) {
    		String firstName = user.getProfile().getFirstName();
    		String lastName = user.getProfile().getLastName();
    		
        	logger.info("***Found user: {} {} and Group:{}", firstName, lastName, adminGroupId);
    		user.addToGroup(adminGroupId);
    		
    		// Update
    		registration.setFirstName(firstName);
    		registration.setLastName(lastName);
    		registration.setGroups(getUsersGroups(user));
    	} else {
    		logger.info("Could not find admin group");
    	}
    	
    	// Return to the Admin page
        return "makeAdmin";
    }
    
    private String getGroupId(GroupList groupList, String groupName) {
    	
    	Iterator<Group> itr = groupList.iterator();
    	while(itr.hasNext()) {
    		Group group = itr.next();
    		
    		logger.info("Group :{}", group);
    		
    		String groupProfileName = group.getProfile().getName();
    		logger.info("Group Profile Name:{}", groupProfileName);
    		if(groupProfileName.equals(groupName)) {
    			// Found a match
    			return group.getId();
    		}
    	}
    	
    	return null;
    }
    
    private String getUsersGroups(User user) {
    	
    	List<String> groups = new ArrayList<>();
    	
    	GroupList groupList = user.listGroups();
    	Iterator<Group> itr = groupList.iterator();
    	while(itr.hasNext()) {
    		Group group = itr.next();    		
    		String groupProfileName = group.getProfile().getName();
    		groups.add(groupProfileName);
    	}
    	
    	return String.join(", ", groups);
    }
}