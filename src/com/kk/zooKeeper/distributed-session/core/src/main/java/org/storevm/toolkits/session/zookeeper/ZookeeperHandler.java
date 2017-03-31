/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper;

import org.apache.zookeeper.ZooKeeper;

/**
 * ZK�ͻ��˲����ӿ�
 * @author Administrator
 * @version $Id: ZookeeperOperate.java, v 0.1 2012-4-8 ����6:07:55 Administrator Exp $
 */
public interface ZookeeperHandler {
    /** ZK��ڵ����� */
    public static final String GROUP_NAME = "/SESSIONS";

    public static final String NODE_SEP   = "/";

    /**
     * ִ�о������
     * 
     * @throws Exception
     */
    public <T> T handle() throws Exception;

    /**
     * ����ZK�ͻ��˶���
     * 
     * @param zookeeper ZK�ͻ���
     */
    public void setZooKeeper(ZooKeeper zookeeper);
}
