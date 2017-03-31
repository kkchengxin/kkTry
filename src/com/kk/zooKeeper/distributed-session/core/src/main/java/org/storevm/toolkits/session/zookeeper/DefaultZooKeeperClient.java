/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.storevm.toolkits.session.pool.ZookeeperPoolManager;

/**
 * Ĭ��ZK�ͻ��˴���ʵ��
 * @author Administrator
 * @version $Id: ZooKeeperClientImpl.java, v 0.1 2012-4-8 ����6:41:19 Administrator Exp $
 */
public class DefaultZooKeeperClient implements ZooKeeperClient {
    /** ��־ */
    private static final Logger    LOGGER = Logger.getLogger(ZooKeeperClient.class);

    /** �������� */
    private static ZooKeeperClient instance;

    /** ZK����� */
    private ZookeeperPoolManager   pool;

    /**
     * ���췽��
     */
    protected DefaultZooKeeperClient() {
        if (pool == null) {
            pool = ZookeeperPoolManager.getInstance();
        }
    }

    /**
     * ���ص�������
     * 
     * @return
     */
    public static ZooKeeperClient getInstance() {
        if (instance == null) {
            instance = new DefaultZooKeeperClient();
        }
        return instance;
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.ZooKeeperClient#execute(org.storevm.toolkits.session.zookeeper.ZookeeperHandler)
     */
    @Override
    public <T> T execute(ZookeeperHandler handler) throws Exception {
        //�ӳ��л�ȡZK���� 
        ZooKeeper zk = pool.borrowObject();
        if (zk != null) {
            try {
                handler.setZooKeeper(zk);
                return (T) handler.handle();
            } catch (KeeperException ex) {
                LOGGER.error("ִ��ZK�ڵ����ʱ�����쳣: ", ex);
            } catch (InterruptedException ex) {
                LOGGER.error("ִ��ZK�ڵ����ʱ�����쳣: ", ex);
            } finally {
                //��ZK���󷵻ض������
                pool.returnObject(zk);
            }
        }
        return (T) null;
    }

}
