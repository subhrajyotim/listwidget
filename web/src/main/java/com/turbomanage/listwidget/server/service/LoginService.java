package com.turbomanage.listwidget.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.googlecode.objectify.Key;
import com.turbomanage.listwidget.domain.AppUser;
import com.turbomanage.listwidget.shared.TooManyResultsException;

/**
 * Server-side class that provides all login-related
 * operations. Called only from server code.
 * 
 * @author turbomanage
 */
public class LoginService
{
	public static final String AUTH_USER = "loggedInUser";

	public static AppUser login(HttpServletRequest req, HttpServletResponse res)
	{
		UserService userService = UserServiceFactory.getUserService();

		// User is logged into GAE
		// Find or add user in our app Datastore
		String userEmail = userService.getCurrentUser().getEmail();
		AppUserDao userDao = new AppUserDao();
		AppUser loggedInUser = findUser(userEmail);
		if (loggedInUser == null)
		{
			// Auto-add user
			loggedInUser = addUser(userEmail);
		}
		req.setAttribute(AUTH_USER, loggedInUser);
		return loggedInUser;
	}

	public static AppUser getLoggedInUser()
	{
		return (AppUser) RequestFactoryServlet.getThreadLocalRequest()
				.getAttribute(AUTH_USER);
	}

	private static AppUser findUser(String userEmail)
	{
		AppUser appUser = null;
		try
		{
			AppUserDao userDao = new AppUserDao();
			// Query for user by email
			appUser = userDao.getByProperty("email", userEmail);
		} catch (TooManyResultsException e)
		{
			throw new RuntimeException(e);
		}
		return appUser;
	}

	private static AppUser addUser(String email)
	{
		AppUserDao userDao = new AppUserDao();
		AppUser newUser = new AppUser(email);
		Key<AppUser> newUserKey = userDao.put(newUser);

		return newUser;
	}

}
