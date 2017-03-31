/**
 * 
 */
package org.storevm.toolkits.session.jetty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mortbay.jetty.servlet.AbstractSessionManager;
import org.mortbay.jetty.servlet.AbstractSessionManager.Session;
import org.storevm.toolkits.session.SessionManager;
import org.storevm.toolkits.session.zookeeper.DefaultZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.ZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.handler.GetDataHandler;
import org.storevm.toolkits.session.zookeeper.handler.PutDataHandler;
import org.storevm.toolkits.session.zookeeper.handler.RemoveDataHandler;
import org.storevm.toolkits.session.zookeeper.handler.RemoveNodeHandler;

/**
 * Jetty�����µ�HttpSession�ӿڵ�ʵ��
 * 
 * @author ̸����
 * @version $Id: JettyDistributedSession.java, v 0.1 2010-12-29 ����10:26:09 ̸���� Exp $
 */
public class JettyDistributedSession extends Session {
    private static final long   serialVersionUID = -6089477971984554624L;

    /** log4j */
    private static final Logger LOGGER           = Logger.getLogger(JettyDistributedSession.class);

    /** Session������ */
    private SessionManager      sessionManager;

    /** ZK�ͻ��˲��� */
    private ZooKeeperClient     client           = DefaultZooKeeperClient.getInstance();

    /**
     * ���췽��
     * 
     * @param sessionManager
     * @param request
     */
    public JettyDistributedSession(AbstractSessionManager sessionManager,
                                   HttpServletRequest request, SessionManager sm) {
        sessionManager.super(request);
        this.sessionManager = sm;
    }

    /**
     * ���췽��
     * @param arg0
     * @param arg1
     */
    public JettyDistributedSession(AbstractSessionManager sessionManager, long create, String id,
                                   SessionManager sm) {
        sessionManager.super(create, id);
        this.sessionManager = sm;
    }

    @Override
    protected Map newAttributeMap() {
        return new HashMap(3);
    }

    /* (non-Javadoc)
     * @see org.mortbay.jetty.servlet.AbstractSessionManager.Session#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String name) {
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

    /* (non-Javadoc)
     * @see org.mortbay.jetty.servlet.AbstractSessionManager.Session#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(String name) {
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

    /**
     * 
     * @see org.mortbay.jetty.servlet.AbstractSessionManager.Session#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(String name, Object value) {
        //û��ʵ�����л��ӿڵ�ֱ�ӷ���
        if (!(value instanceof Serializable)) {
            LOGGER.warn("����[" + value + "]û��ʵ��Serializable�ӿڣ��޷����浽�ֲ�ʽSession��");
            return;
        }
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

    /** 
     * @see org.mortbay.jetty.servlet.AbstractSessionManager.Session#invalidate()
     */
    @Override
    public void invalidate() throws IllegalStateException {
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
        if (sessionManager != null) {
            sessionManager.removeHttpSession(this);
        }
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
