/**
 * Storevm.org Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package org.storevm.toolkits.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.storevm.toolkits.component.LifeCycle;

/**
 * HttpSession�������ڹ������ӿ�
 * @author ̸����
 * @version $Id: SessionManager.java, v 0.1 2010-12-29 ����08:57:26 ̸���� Exp $
 */
public interface SessionManager extends LifeCycle {
    /**Cookie�Ĺ���ʱ�䣬Ĭ��1��*/
    public static final int COOKIE_EXPIRY = 365 * 24 * 60 * 60;

    /**
     * ����ָ��ID��HttpSession����
     * @param id Session ID
     * @param request HTTP����
     * @return
     */
    public HttpSession getHttpSession(String id, HttpServletRequest request);

    /**
     * ����һ���µ�HttpSession����
     * @param request HTTP����
     * @return
     */
    public HttpSession newHttpSession(HttpServletRequest request);

    /**
     * ��ȡ��������е�Session�����ID��һ���Ǵ�Cookie�л�ȡ
     * @param request HTTP����
     * @return
     */
    public String getRequestSessionId(HttpServletRequest request);

    /**
     * ��һ��HttpSession����������������
     * @param session HTTP Session����
     * @param request HTTP����
     */
    public void addHttpSession(HttpSession session);

    /**
     * ɾ��Session
     * @param session
     */
    public void removeHttpSession(HttpSession session);

    /**
     * ����һ��Ψһ��Session ID
     * @return 
     */
    public String getNewSessionId(HttpServletRequest request);

    /**
     * ����Servlet������
     * @return
     */
    public ServletContext getServletContext();

    /**
     * ����Servlet������
     * @param sc
     */
    public void setServletContext(ServletContext sc);

    /**
     * ����HttpServletResponse����
     * @return
     */
    public HttpServletResponse getResponse();

    /**
     * ����HttpServletResponse����
     * @param response
     */
    public void setHttpServletResponse(HttpServletResponse response);
}
