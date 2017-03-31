/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

/**
 * ����ʵ��
 * @author Administrator
 * @version $Id: AbstractZookeeperExecute.java, v 0.1 2012-4-8 ����6:16:01 Administrator Exp $
 */
public abstract class AbstractZookeeperHandler implements ZookeeperHandler {
    /** ��־ */
    protected static final Logger LOGGER = Logger.getLogger(ZookeeperHandler.class);

    /** ZK�ͻ��� */
    protected ZooKeeper           zookeeper;

    /**
     * �ڵ�ID
     */
    protected String              id;

    /**
     * ���췽��
     */
    public AbstractZookeeperHandler(String id) {
        this.id = id;
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.ZookeeperHandler#setZooKeeper(org.apache.zookeeper.ZooKeeper)
     */
    @Override
    public void setZooKeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }
}
