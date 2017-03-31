/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.pool;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.storevm.toolkits.session.config.Configuration;
import org.storevm.toolkits.session.helper.ConnectionWatcher;

/**
 * Zookeeperʵ������أ�����һ��Zookeeperʵ������һ��Socket���ӣ����Խ�Zookeeperʵ���ػ�����ʵ���������е�����
 * @author xiangqing.tan
 * @version $Id: ZookeeperPoolableObjectFactory.java, v 0.1 2012-4-1 ����03:52:05 xiangqing.tan Exp $
 */
public class ZookeeperPoolableObjectFactory implements PoolableObjectFactory<ZooKeeper> {
    private static final Logger LOGGER = Logger.getLogger(ZookeeperPoolableObjectFactory.class);

    /** ������Ϣ���� */
    private Configuration       config;

    /**
     * ���췽��
     * @param config
     */
    public ZookeeperPoolableObjectFactory(Configuration config) {
        this.config = config;
    }

    @Override
    public ZooKeeper makeObject() throws Exception {
        //����һ���µ�zkʵ��
        ConnectionWatcher cw = new ConnectionWatcher();

        //���ӷ����
        String servers = config.getString(Configuration.SERVERS);
        int timeout = NumberUtils.toInt(config.getString(Configuration.TIMEOUT));
        ZooKeeper zk = cw.connection(servers, timeout);
        if (zk != null) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("ʵ����ZK�ͻ��˶���zk.sessionId=" + zk.getSessionId());
            }
        } else {
            LOGGER.warn("ʵ����ZK�ͻ��˶���ʧ��");
        }
        return zk;
    }

    @Override
    public void destroyObject(ZooKeeper obj) throws Exception {
        if (obj != null) {
            obj.close();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("ZK�ͻ��˶��󱻹رգ�zk.sessionId=" + obj.getSessionId());
            }
        }
    }

    @Override
    public boolean validateObject(ZooKeeper obj) {
        if (obj != null && obj.getState() == States.CONNECTED) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("ZK�ͻ��˶�����֤ͨ����zk.sessionId=" + obj.getSessionId());
            }
            return true;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("ZK�ͻ��˶�����֤��ͨ����zk.sessionId=" + obj.getSessionId());
        }
        return false;
    }

    @Override
    public void activateObject(ZooKeeper obj) throws Exception {
    }

    @Override
    public void passivateObject(ZooKeeper obj) throws Exception {
    }

}
