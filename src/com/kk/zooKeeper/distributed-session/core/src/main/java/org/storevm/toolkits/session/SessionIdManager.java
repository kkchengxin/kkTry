/**
 * Storevm.org Inc.
 * Copyright (c) 2004-2011 All Rights Reserved.
 */
package org.storevm.toolkits.session;

import javax.servlet.http.HttpServletRequest;

import org.storevm.toolkits.component.LifeCycle;

/**
 * Session ID������
 * @author ̸����
 * @version $Id: SessionIdManager.java, v 0.1 2011-1-1 ����11:49:44 ̸���� Exp $
 */
public interface SessionIdManager extends LifeCycle {
    /**
     * �����µ�Session ID
     * @param request
     * @param created
     * @return
     */
    public String newSessionId(HttpServletRequest request, long created);
}
