package com.aem.traning.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@Component(service=Servlet.class,
property={
        Constants.SERVICE_DESCRIPTION + "= Simple Demo Servlet to get User Properties ",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.paths="+ "/bin/get/user/properties"
})

public class UserServlet extends SlingSafeMethodsServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	ResourceResolver resourceResolver;
	@Reference
	private ResourceResolverFactory resourceFactory;
	
	@Reference
	ResourceResolverFactory resolverFactory;

	ResourceResolver adminResolver = null;
	
	//Map<String,Object> paramMap = new HashMap<String,Object>();
	Map<String, String> userProperties = new HashMap<>();
	
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)throws ServletException,IOException{
		response.setContentType("application/json");
		try {
			
			ResourceResolver resolver = ((SlingHttpServletRequest) request).getResourceResolver();
	        String userId = resolver.getUserID();
			response.getWriter().println("Session User is " + userId);
			
			UserManager userManager = request.getResource().adaptTo(UserManager.class);
			final User user = (User)userManager.getAuthorizable(resolver.getUserID());
			
			response.getWriter().println("userID).." + user.getID());
			response.getWriter().println("user.isAdmin().."+user.isAdmin());
			//response.getWriter().write("getName.." + user.getPrincipal().getName());
			
			String[] userProfileName = { "familyName", "givenName", "aboutMe", "email", "secondEmail", "gender", "streetAddress", "city", "region", "language", "jobTitle", "url", "smartRendering" };
			
			for (String name : userProfileName) {
				String value = user.getProperty("./profile/" + name)!=null ? user.getProperty("./profile/" + name)[0].getString() : null;
				userProperties.put(name, value);
			}
				
			/*String familyName = user.getProperty("./profile/familyName")!=null ? user.getProperty("./profile/familyName")[0].getString() : null;
			String givenName = user.getProperty("./profile/givenName")!=null?user.getProperty("./profile/givenName")[0].getString():null;
			String aboutMe = user.getProperty("./profile/aboutMe")!=null?user.getProperty("./profile/aboutMe")[0].getString():null;
			String email = user.getProperty("./profile/email")!=null?user.getProperty("./profile/email")[0].getString():null;
			String secondEmail = user.getProperty("./profile/secondEmail")!=null?user.getProperty("./profile/secondEmail")[0].getString():null;
			userProperties.put("familyName", familyName);			
			userProperties.put("givenName", givenName);*/
			
			
			// Display Output
			String userProfile = new Gson().toJson(userProperties);
			response.getWriter().println(userProfile);

			
		} catch (Exception e) {
			response.getWriter().println("Can't get User Information");
			logger.error(e.getMessage());
		}
		response.getWriter().close();
	}
	
}