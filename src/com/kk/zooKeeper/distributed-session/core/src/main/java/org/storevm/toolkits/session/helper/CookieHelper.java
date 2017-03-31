/**
 * Storevm.org Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package org.storevm.toolkits.session.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * ����Cookie��������
 * @author ̸����
 * @version $Id: CookieHelper.java, v 0.1 2010-12-29 ����09:03:39 ̸���� Exp $
 */
public class CookieHelper {
    private static final String DISTRIBUTED_SESSION_ID = "STOREVMJSESSIONID";
    protected static Logger     log                    = Logger.getLogger(CookieHelper.class);

    /**
     * ��Session IDд���ͻ��˵�Cookie��
     * @param id Session ID
     * @param response HTTP��Ӧ
     * @return
     */
    public static Cookie writeSessionIdToNewCookie(String id, HttpServletResponse response,
                                                   int expiry) {
        Cookie cookie = new Cookie(DISTRIBUTED_SESSION_ID, id);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
        return cookie;
    }

    /**
     * ��Session IDд���ͻ��˵�Cookie��
     * @param id Session ID
     * @param response HTTP��Ӧ
     * @return
     */
    public static Cookie writeSessionIdToCookie(String id, HttpServletRequest request,
                                                HttpServletResponse response, int expiry) {
        //�Ȳ���
        Cookie cookie = findCookie(DISTRIBUTED_SESSION_ID, request);
        //��������ڣ����½�һ��
        if (cookie == null) {
            return writeSessionIdToNewCookie(id, response, expiry);
        } else {
            //ֱ�Ӹ�дcookie��ֵ
            cookie.setValue(id);
            cookie.setMaxAge(expiry);
            response.addCookie(cookie);
        }
        return cookie;
    }

    /**
     * ��ѯָ�����Ƶ�Cookieֵ
     * @param name cookie����
     * @param request HTTP����
     * @return
     */
    public static String findCookieValue(String name, HttpServletRequest request) {
        Cookie cookie = findCookie(name, request);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * ��ѯָ�����Ƶ�Cookie
     * @param name cookie����
     * @param request HTTP����
     * @return
     */
    public static Cookie findCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        // ��������
        for (int i = 0, n = cookies.length; i < n; i++) {
            if (cookies[i].getName().equalsIgnoreCase(name)) {
                return cookies[i];
            }
        }
        return null;
    }

    /**
     * ��Cookie�в���Session ID
     * @param request HTTP����
     * @return
     */
    public static String findSessionId(HttpServletRequest request) {
        return findCookieValue(DISTRIBUTED_SESSION_ID, request);
    }
}
