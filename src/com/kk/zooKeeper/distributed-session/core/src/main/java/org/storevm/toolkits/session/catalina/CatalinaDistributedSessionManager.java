/**
 * Storevm.org Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package org.storevm.toolkits.session.catalina;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.math.NumberUtils;
import org.storevm.toolkits.session.DefaultSessionManager;
import org.storevm.toolkits.session.config.Configuration;
import org.storevm.toolkits.session.helper.CookieHelper;
import org.storevm.toolkits.session.metadata.SessionMetaData;
import org.storevm.toolkits.session.zookeeper.handler.CreateNodeHandler;
import org.storevm.toolkits.session.zookeeper.handler.RemoveNodeHandler;
import org.storevm.toolkits.session.zookeeper.handler.UpdateMetadataHandler;

/**
 * Tomcat�����µ�Session������
 * @author xiangqing.tan
 * @version $Id: CatalinaDistributedSessionManager.java, v 0.1 2010-12-31 ����05:26:13 xiangqing.tan Exp $
 */
public class CatalinaDistributedSessionManager extends DefaultSessionManager {
    /**
     * ���췽��
     * @param config
     */
    public CatalinaDistributedSessionManager(ServletContext sc) {
        super(sc);
    }

    @Override
    public HttpSession getHttpSession(String id, HttpServletRequest request) {
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
            CatalinaDistributedSession sess = new CatalinaDistributedSession(this, id);
            sess.access(); //��ʾ�Ѿ������ʹ���
            session = new CatalinaDistributedSessionFacade(sess);
            addHttpSession(session);
            return session;
        }
    }

    @Override
    public HttpSession newHttpSession(HttpServletRequest request) {
        String id = getNewSessionId(request); //��ȡ�µ�Session ID
        CatalinaDistributedSession sess = new CatalinaDistributedSession(this, id);
        HttpSession session = new CatalinaDistributedSessionFacade(sess);
        // дcookie
        Cookie cookie = CookieHelper.writeSessionIdToCookie(id, request, getResponse(),
            COOKIE_EXPIRY);
        if (cookie != null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Wrote sid to Cookie,name:[" + cookie.getName() + "],value:["
                            + cookie.getValue() + "]");
            }
        }
        //����Ԫ����
        SessionMetaData metadata = new SessionMetaData();
        metadata.setId(id);
        Long sessionTimeout = NumberUtils.toLong(config.getString(Configuration.SESSION_TIMEOUT));
        metadata.setMaxIdle(sessionTimeout * 60 * 1000); //ת���ɺ���
        //��ZooKeeper�������ϴ���session�ڵ㣬�ڵ�����ΪSession ID
        try {
            client.execute(new CreateNodeHandler(id, metadata));
        } catch (Exception ex) {
            LOGGER.error("�����ڵ�ʱ�����쳣��", ex);
        }
        addHttpSession(session);
        return session;
    }
}
