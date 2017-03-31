/**
 * Storevm.org Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package org.storevm.toolkits.session.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.storevm.toolkits.session.catalina.CatalinaDistributedSessionManager;

/**
 * ����Tomcat�����ķֲ�ʽSession�Ĺ�����ʵ��
 * @author xiangqing.tan
 * @version $Id: CatalinaDistributedSessionFilter.java, v 0.1 2010-12-31 ����03:07:55 xiangqing.tan Exp $
 */
public class CatalinaDistributedSessionFilter extends DistributedSessionFilter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        // ʵ����Tomcat�����µ�Session������
        this.sessionManager = new CatalinaDistributedSessionManager(
            filterConfig.getServletContext());
        try {
            sessionManager.start(); // ������ʼ��
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("DistributedSessionFilter.init completed.");
            }
        } catch (Exception ex) {
            LOGGER.error("��������ʼ��ʧ�ܣ�", ex);
        }
    }

    /** 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                                                             throws IOException,
                                                                                             ServletException {
        //����Response
        sessionManager.setHttpServletResponse((HttpServletResponse) response);
        super.doFilter(request, response, chain);
    }

}
