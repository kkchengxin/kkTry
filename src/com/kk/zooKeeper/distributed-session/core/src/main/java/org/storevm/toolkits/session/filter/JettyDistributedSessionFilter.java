/**
 * 
 */
package org.storevm.toolkits.session.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.storevm.toolkits.session.jetty.JettyDistributedSessionManager;

/**
 * ����Jetty�����ķֲ�ʽSession�Ĺ�����ʵ��
 * 
 * @author ̸����
 * @version $Id: JettyDistributedSessionFilter.java, v 0.1 2010-12-29 ����09:10:29
 *          ̸���� Exp $
 */
public class JettyDistributedSessionFilter extends DistributedSessionFilter {
    private Logger log = Logger.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        // ʵ����Jetty�����µ�Session������
        sessionManager = new JettyDistributedSessionManager(filterConfig.getServletContext());
        try {
            sessionManager.start(); // ������ʼ��
            log.debug("DistributedSessionFilter.init completed.");
        } catch (Exception e) {
            log.error(e);
        }
    }
}
