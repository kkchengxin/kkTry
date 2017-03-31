/**
 * Storevm.org Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package org.storevm.toolkits.session.catalina;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.storevm.toolkits.session.SessionManager;
import org.storevm.toolkits.session.zookeeper.DefaultZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.ZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.handler.GetDataHandler;
import org.storevm.toolkits.session.zookeeper.handler.GetNodeNames;
import org.storevm.toolkits.session.zookeeper.handler.PutDataHandler;
import org.storevm.toolkits.session.zookeeper.handler.RemoveDataHandler;
import org.storevm.toolkits.session.zookeeper.handler.RemoveNodeHandler;

/**
 * Tomcat�����µ�HttpSessionʵ��
 * @author xiangqing.tan
 * @version $Id: CatalinaDistributedSession.java, v 0.1 2010-12-31 ����05:23:45 xiangqing.tan Exp $
 */
@SuppressWarnings("deprecation")
public class CatalinaDistributedSession implements HttpSession {
    /** log4j */
    private static final Logger LOGGER = Logger.getLogger(CatalinaDistributedSession.class);

    /**Session������*/
    private SessionManager      sessionManager;
    /**Session ID*/
    private String              id;
    /**Session����ʱ��*/
    private long                creationTm;
    /**Session���һ�η���ʱ��*/
    private long                lastAccessedTm;
    /**Session��������ʱ����*/
    private int                 maxInactiveInterval;
    /**�Ƿ����½�Session*/
    private boolean             newSession;

    /** ZK�ͻ��˲��� */
    private ZooKeeperClient     client = DefaultZooKeeperClient.getInstance();

    /**
     * ���췽��
     * @param sessionManager
     */
    public CatalinaDistributedSession(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.creationTm = System.currentTimeMillis();
        this.lastAccessedTm = this.creationTm;
        this.newSession = true;
    }

    /**
     * ���췽��,ָ��ID
     * @param sessionManager
     * @param id
     */
    public CatalinaDistributedSession(SessionManager sessionManager, String id) {
        this(sessionManager);
        this.id = id;
    }

    @Override
    public long getCreationTime() {
        return creationTm;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTm;
    }

    @Override
    public ServletContext getServletContext() {
        return sessionManager.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    @Deprecated
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        access();
        //��ȡsession ID
        String id = getId();
        if (StringUtils.isNotBlank(id)) {
            //����Session�ڵ��µ�����
            try {
                return client.execute(new GetDataHandler(id, name));
            } catch (Exception ex) {
                LOGGER.error("����getAttribute����ʱ�����쳣��", ex);
            }
        }
        return null;
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        access();
        //��ȡsession ID
        String id = getId();
        if (StringUtils.isNotBlank(id)) {
            //����Session�ڵ��µ���������
            try {
                List<String> names = client.execute(new GetNodeNames(id));
                if (names != null) {
                    return Collections.enumeration(names);
                }
            } catch (Exception ex) {
                LOGGER.error("����getAttributeNames����ʱ�����쳣��", ex);
            }

        }
        return null;
    }

    @Override
    public String[] getValueNames() {
        List<String> names = new ArrayList<String>();
        Enumeration n = getAttributeNames();
        while (n.hasMoreElements()) {
            names.add((String) n.nextElement());
        }
        return names.toArray(new String[] {});
    }

    @Override
    public void setAttribute(String name, Object value) {
        //û��ʵ�����л��ӿڵ�ֱ�ӷ���
        if (!(value instanceof Serializable)) {
            LOGGER.warn("����[" + value + "]û��ʵ��Serializable�ӿڣ��޷����浽�ֲ�ʽSession��");
            return;
        }
        access();
        //��ȡsession ID
        String id = getId();
        if (StringUtils.isNotBlank(id)) {
            //��������ӵ�ZooKeeper��������
            try {
                value = client.execute(new PutDataHandler(id, name, (Serializable) value));
            } catch (Exception ex) {
                LOGGER.error("����setAttribute����ʱ�����쳣��", ex);
            }
        }
        //����Session�ļ�����
        fireHttpSessionBindEvent(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        access();
        Object value = null;
        //��ȡsession ID
        String id = getId();
        if (StringUtils.isNotBlank(id)) {
            //ɾ��Session�ڵ��µ�����
            try {
                value = client.execute(new RemoveDataHandler(id, name));
            } catch (Exception ex) {
                LOGGER.error("����removeAttribute����ʱ�����쳣��", ex);
            }
        }
        //����Session�ļ�����
        fireHttpSessionUnbindEvent(name, value);
    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public void invalidate() {
        //��ȡsession ID
        String id = getId();
        if (StringUtils.isNotBlank(id)) {
            //ɾ��Session�ڵ�
            try {
                Map<String, Object> sessionMap = client.execute(new RemoveNodeHandler(id));
                if (sessionMap != null) {
                    Set<String> keys = sessionMap.keySet();
                    for (String key : keys) {
                        Object value = sessionMap.get(key);
                        fireHttpSessionUnbindEvent(key, value);
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("����invalidate����ʱ�����쳣��", ex);
            }
        }
        //ɾ�����������е�Session����
        sessionManager.removeHttpSession(this);
    }

    @Override
    public boolean isNew() {
        return newSession;
    }

    /**
     * ������
     */
    public void access() {
        this.newSession = false;
        this.lastAccessedTm = System.currentTimeMillis();
    }

    /**
     * ����Session���¼�
     * @param value
     */
    protected void fireHttpSessionBindEvent(String name, Object value) {
        //����Session�ļ�����
        if (value != null && value instanceof HttpSessionBindingListener) {
            HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, name, value);
            ((HttpSessionBindingListener) value).valueBound(event);
        }
    }

    /**
     * ����Session���¼�
     * @param value
     */
    protected void fireHttpSessionUnbindEvent(String name, Object value) {
        //����Session�ļ�����
        if (value != null && value instanceof HttpSessionBindingListener) {
            HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, name, value);
            ((HttpSessionBindingListener) value).valueUnbound(event);
        }
    }
}
