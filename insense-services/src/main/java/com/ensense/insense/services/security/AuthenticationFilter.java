package com.ensense.insense.services.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFilter implements Filter {
	private static Logger logger = Logger.getLogger(AuthenticationFilter.class);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest requestIn, ServletResponse responseIn,
			FilterChain chain) throws IOException, ServletException {
		//logger.debug("Entry: doFilter");
		
		HttpServletRequest request = (HttpServletRequest) requestIn;
		HttpServletResponse response = (HttpServletResponse) responseIn;
		
		/*if ( isAlreadyLoggedInUser(request) ){

		}*/
		
		if ( isLoginFromSSO(request) ){
			handleEnvironmentLogin(request, response);
		} else if ( isLoginFromLocal(request) ){
			handleLocalSetupLogin(request, response);
		}

		//logger.debug("Exit: doFilter");
		chain.doFilter(request, response);
	}

	private void handleLocalSetupLogin(HttpServletRequest request,
			HttpServletResponse response) {

		if (StringUtils.isNotEmpty(request.getParameter("loginUserName"))){
			HttpSession session = request.getSession(true);
			session.setAttribute("loginUserName", request.getParameter("loginUserName"));
		}
		
	}

	private boolean isLoginFromLocal(HttpServletRequest request) {
		//logger.info("Login User Name :"+request.getParameter("loginUserName"));
		if ( request.getParameter("loginUserName") != null && request.getParameter("loginUserName").trim().length() > 0 ){
			return true;
		}
		return false;
	}

	private void handleEnvironmentLogin(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("redirectUserId");
		
		if (StringUtils.isNotEmpty(request.getHeader("UID"))) {
			HttpSession session = request.getSession(true);
			session.setAttribute("loginUserName", request.getHeader("UID"));
			session.setAttribute("emailId", request.getHeader("email"));
			session.setAttribute("environmentLogin", "true");
		}else if ( null != userId && userId.length() > 0 ){
			request.getSession().removeAttribute("userid");
			request.getSession().invalidate();
			HttpSession session = request.getSession(true);
			session.setAttribute("loginUserName", request.getParameter("redirectUserId"));
			session.setAttribute("environmentLogin", "true");
		}
		
	}

	private boolean isLoginFromSSO(HttpServletRequest request) {
		if ( null != request.getParameter("redirectUserId") && request.getParameter("redirectUserId").trim().length() > 0 ){
			return true;
		}
		if (StringUtils.isNotEmpty(request.getHeader("UID"))) {
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
