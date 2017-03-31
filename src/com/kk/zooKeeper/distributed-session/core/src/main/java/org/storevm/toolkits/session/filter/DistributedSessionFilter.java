/**
 * Storevm.org Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
package org.storevm.toolkits.session.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.storevm.toolkits.session.SessionManager;
import org.storevm.toolkits.session.config.Configuration;
import org.storevm.toolkits.session.pool.ZookeeperPoolManager;
import org.storevm.toolkits.session.servlet.ContainerRequestWrapper;
import org.storevm.toolkits.session.zookeeper.DefaultZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.ZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.handler.CreateGroupNodeHandler;

/**
 * �ֲ�ʽSession����������ʵ���࣬����ʵ�ֹ��з���
 * @author ̸����
 * @version $Id: DistributedSessionFilter.java, v 0.1 2010-12-29 ����09:12:46 ̸���� Exp $
 */
public abstract class DistributedSessionFilter implements Filter {
    /** log4j */
    protected static final Logger LOGGER = Logger.getLogger(DistributedSessionFilter.class);

    /**Session������*/
    protected SessionManager      sessionManager;

    /** ZK�ͻ��˲��� */
    protected ZooKeeperClient     client = DefaultZooKeeperClient.getInstance();

    /**
     * ��ʼ��
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //��ʼ��ϵͳ��������
        Configuration conf = Configuration.getInstance();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("1. ��ȡϵͳ�������Գɹ���" + conf);
        }

        //���������Է�����������
        ServletContext sc = filterConfig.getServletContext();
        sc.setAttribute(Configuration.CFG_NAME, conf);

        //��ʼ��ZK�ͻ��˶����
        ZookeeperPoolManager.getInstance().init(conf);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("2. ��ʼ��ZK�ͻ��˶�������");
        }

        try {
            client.execute(new CreateGroupNodeHandler());
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("3. ����SESSIONS��ڵ����");
            }
        } catch (Exception ex) {
            LOGGER.error("������ڵ�ʱ�����쳣��", ex);
        }
    }

    /**
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                                                             throws IOException,
                                                                                             ServletException {
        //Request�İ�װ����������дHttpServletRequest��getSession����
        HttpServletRequest req = new ContainerRequestWrapper(request, sessionManager);
        chain.doFilter(req, response);
    }

    /**
     * ����
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        //�ر�Session������
        if (sessionManager != null) {
            try {
                sessionManager.stop();
            } catch (Exception ex) {
                LOGGER.error("�ر�Session������ʱ�����쳣��", ex);
            }
        }

        //�ر�ZKʵ����
        ZookeeperPoolManager.getInstance().close();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("DistributedSessionFilter.destroy completed.");
        }
    }

}
