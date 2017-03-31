/**
 * 
 */
package org.storevm.toolkits.session.jetty;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.servlet.AbstractSessionManager;
import org.mortbay.jetty.servlet.AbstractSessionManager.Session;
import org.storevm.toolkits.session.DefaultSessionManager;
import org.storevm.toolkits.session.config.Configuration;
import org.storevm.toolkits.session.helper.CookieHelper;
import org.storevm.toolkits.session.metadata.SessionMetaData;
import org.storevm.toolkits.session.zookeeper.handler.CreateNodeHandler;
import org.storevm.toolkits.session.zookeeper.handler.RemoveNodeHandler;
import org.storevm.toolkits.session.zookeeper.handler.UpdateMetadataHandler;

/**
 * Jetty�����µ�Session������
 * 
 * @author ̸����
 * @version $Id: JettyDistributedSessionManager.java, v 0.1 2010-12-29 ����09:48:13 ̸���� Exp $
 */
public class JettyDistributedSessionManager extends DefaultSessionManager {
    /**
     * ���췽��
     * @param config
     */
    public JettyDistributedSessionManager(ServletContext sc) {
        super(sc);
    }

    /**
     * ��ȡ�����е�session
     * 
     * @param id
     * @return
     */
    @Override
    public HttpSession getHttpSession(String id, HttpServletRequest request) {
        //���ͼ��
        if (!(request instanceof Request)) {
            LOGGER.warn("����Jetty�����µ�Request����");
            return null;
        }
        //��HttpServletRequestת����Jetty������Request����
        Request req = (Request) request;
        HttpSession session = sessions.get(id);
        //ZooKeeper�������ϲ���ָ���ڵ��Ƿ���Ч�������½ڵ�Ԫ����
        Boolean valid = Boolean.FALSE;
        try {
            valid = client.execute(new UpdateMetadataHandler(id));
        } catch (Exception ex) {
            LOGGER.error("���½ڵ�Ԫ����ʱ�����쳣��", ex);
        }
        //���Ϊfalse����ʾ���������޸�Session�ڵ㣬��Ҫ���´���(����null)
        if (!valid) {
            //ɾ�����صĸ���
            if (session != null) {
                session.invalidate();
            } else {
                //ɾ��ZooKeeper�Ϲ¶�Session�ڵ�
                try {
                    client.execute(new RemoveNodeHandler(id));
                } catch (Exception ex) {
                    LOGGER.error("ɾ���ڵ�Ԫ����ʱ�����쳣��", ex);
                }
            }
            return null;
        } else {
            //������ڣ���ֱ�ӷ���
            if (session != null) {
                return session;
            }
            //���򴴽�ָ��ID��Session������(����ͬ���ֲ�ʽ�����е����������ϵ�Session���ظ���)
            session = new JettyDistributedSession((AbstractSessionManager) req.getSessionManager(),
                System.currentTimeMillis(), id, this);
            addHttpSession(session);
            return session;
        }
    }

    /**
     * ����һ���µ�session
     * 
     * @param request
     * @return
     */
    @Override
    public HttpSession newHttpSession(HttpServletRequest request) {
        //���ͼ��
        if (!(request instanceof Request)) {
            LOGGER.warn("����Jetty�����µ�Request����");
            return null;
        }
        //��HttpServletRequestת����Jetty������Request����
        Request req = (Request) request;
        Session session = new JettyDistributedSession(
            (AbstractSessionManager) req.getSessionManager(), request, this);
        String id = session.getId();
        // дcookie
        Cookie cookie = CookieHelper.writeSessionIdToCookie(id, req, req.getConnection()
            .getResponse(), COOKIE_EXPIRY);
        if (cookie != null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Wrote sid to Cookie,name:[" + cookie.getName() + "],value:["
                            + cookie.getValue() + "]");
            }
        }
        //��ZooKeeper�������ϴ���session�ڵ㣬�ڵ�����ΪSession ID
        //����Ԫ����
        SessionMetaData metadata = new SessionMetaData();
        metadata.setId(id);
        long sessionTimeout = NumberUtils.toLong(config.getString(Configuration.SESSION_TIMEOUT)) * 60 * 1000;
        metadata.setMaxIdle(sessionTimeout); //ת���ɺ���
        try {
            client.execute(new CreateNodeHandler(id, metadata));
        } catch (Exception ex) {
            LOGGER.error("�����ڵ�ʱ�����쳣��", ex);
        }
        addHttpSession(session);
        return session;
    }
}
