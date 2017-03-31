package com.kk.rainbow.core.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DateQJFilter implements Filter {
	private String starttime;
	private String endtime;
	private String tourl;

	public void destroy() {
	}

	public void doFilter(ServletRequest paramServletRequest,
			ServletResponse paramServletResponse, FilterChain paramFilterChain)
			throws IOException, ServletException {
		HttpServletRequest localHttpServletRequest = (HttpServletRequest) paramServletRequest;
		HttpServletResponse localHttpServletResponse = (HttpServletResponse) paramServletResponse;
		System.out.println(this.starttime + ":" + this.endtime + ":tourl="
				+ this.tourl);
		long l = System.currentTimeMillis();
		Date localDate1 = new Date(this.starttime);
		Date localDate2 = new Date(this.endtime);
		if ((l >= localDate1.getTime()) && (l <= localDate2.getTime())) {
			localHttpServletResponse.sendRedirect(this.tourl);
			return;
		}
		System.out.println("DateQJFilter passed! Please continue!");
		paramFilterChain.doFilter(localHttpServletRequest,
				localHttpServletResponse);
	}

	public void init(FilterConfig paramFilterConfig) throws ServletException {
		this.starttime = paramFilterConfig.getInitParameter("starttime");
		this.endtime = paramFilterConfig.getInitParameter("endtime");
		this.tourl = paramFilterConfig.getInitParameter("tourl");
	}
}