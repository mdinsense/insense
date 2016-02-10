package common.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.MenuAccess;
import com.cts.mint.common.service.MenuService;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.PropertyFileReader;
import com.cts.mint.common.utils.UserServiceUtils;

@Service
public class CommonInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger
			.getLogger(CommonInterceptor.class);
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MessageSource configProperties;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		/*Enumeration<?> e = request.getParameterNames();

		while (e.hasMoreElements()) {
			logger.info("Request Parameter :" + e.nextElement());
		}*/

	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//logger.debug("preHandle, request->" + request.getRequestURL());
		if(UserServiceUtils.isConfigPage(request) || UserServiceUtils.guardianPage(request) || UserServiceUtils.mintAccessPage(request) ) {
			return true;
		}
		
		if ( isLoginPage(request) ){
			if ( null != request.getSession().getAttribute("environmentLogin") || null != request.getParameter("loginUserName") ){
				request.getSession().removeAttribute("environmentLogin");
				UserServiceUtils.addUserDetailsToSession(request, userService, menuService);
				redirectToHomePage(request, response);
			}else if ( UserServiceUtils.isUserDetailsAvailableInSession(request) ){
				redirectToHomePage(request, response);
			}else if ( ! isLocalUrl(request)) {
				//redirectToLoginPage(request, response);
				redirectToGetAccessPage(request, response);
			}
			return true;
		}

		if ( isUserNameAvailable(request) && isValidMintUser(request) ){
			if ( ! UserServiceUtils.isUserDetailsAvailableInSession(request) ){
				UserServiceUtils.addUserDetailsToSession(request, userService, menuService);
			}
		}else {
			//redirectToLoginPage(request, response);
			logger.info("user don't have access");
			if ( isLoginPage(request) ){
				redirectToGetAccessPage(request, response);
			}else {
				//redirectToLoginPage(request, response);
				redirectToGetAccessPage(request, response);
			}
			return false;
		}
		
		if ( UserServiceUtils.isUserDetailsAvailableInSession(request) ){
			if ( isUserHasAccessToUrl(request) ){
				
			}else{
				redirectToHomePage(request, response);
				return false;
			}
		}else{
			redirectToGetAccessPage(request, response);
			return false;
		}
		/*Enumeration<?> e = request.getParameterNames();

		while (e.hasMoreElements()) {
			logger.info("Request Parameter :" + e.nextElement());
		}*/

		return true;
	}

	private boolean isLocalUrl(HttpServletRequest request) {
		if ( null != request && null != request.getRequestURL() &&  request.getRequestURL().toString().contains("/localhost:")){
			return true;
		}
		return false;
	}

	private boolean isValidMintUser(HttpServletRequest request) {
		String userName = UserServiceUtils.getUserNameFromSession(request);
		Users mintUser = userService.getMintUser(userName);
		
		if ( null == mintUser || mintUser.getUserId() == 0 ){
			return false;
		}
		
		return true;
	}

	private boolean isLoginPage(HttpServletRequest request) {
		if ( request.getRequestURL().toString().endsWith("login.ftl") || request.getRequestURL().toString().endsWith("mint/") || request.getRequestURL().toString().endsWith("mint") 
				|| request.getRequestURL().toString().endsWith("mintTest/") || request.getRequestURL().toString().contains("redirectUserId=") ){
			return true;
		}
		return false;
	}

	private void redirectToGetAccessPage(HttpServletRequest request,
			HttpServletResponse response) {
		redirectToPage(request, response, "/GetAccess.ftl");
		return;
		
	}

	private void redirectToLoginPage(HttpServletRequest request,
			HttpServletResponse response) {
		String loginUrl = PropertyFileReader.getProperty(configProperties, "mint.login.url");
		
		if ( isLocalUrl(request) ){
			loginUrl = PropertyFileReader.getProperty(configProperties, "mint.login.local.url");
		}
		redirectToUrl(request, response, loginUrl);
		
	}

	private void redirectToHomePage(HttpServletRequest request,
			HttpServletResponse response) {
		redirectToPage(request, response, "/Home.ftl");
		return;
		
	}
	
	private void redirectToUrl(HttpServletRequest request,
			HttpServletResponse response, String uri){
		//logger.debug("uri :"+uri);

		request.setAttribute("redirectUrl", uri);
		redirectToPage(request, response, "/redirectUrl.ftl");

		return;
	}
	
	private void redirectToPage(HttpServletRequest request,
			HttpServletResponse response, String uri){
		//logger.debug("uri :"+uri);
		ServletContext context = request.getServletContext();
		RequestDispatcher dispatcher = context
				.getRequestDispatcher(uri);

		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		return;
	}
	private boolean isUserNameAvailable(HttpServletRequest request) {
		if ( UserServiceUtils.getUserNameFromSession(request).trim().length() > 0 ){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean isUserHasAccessToUrl(HttpServletRequest request) {
		String menuAction = getActionForTheUrl(request.getRequestURL()
				.toString());

		if ( null == menuAction ){
			return false;
		}

		List<MenuAccess> menuAccess = new ArrayList<MenuAccess>();
		menuAccess = (List<MenuAccess>)request.getSession().getAttribute("currentUserMenuAccess");
		
		for ( MenuAccess menu : menuAccess){
			if ( null != menu.getMenuAction() && menu.getMenuAction().equals(menuAction) ){
				return true;
			}
		}

		return false;
	}
	
	private String getActionForTheUrl(String url) {
		String action = "";

		if (null != url && url.lastIndexOf("//") > 0
				&& url.lastIndexOf("//") > url.length()) {
			action = url.substring(url.lastIndexOf("//"), url.length());
		}
		return action;
	}

	
}
