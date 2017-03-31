/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.pool;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;
import org.storevm.toolkits.session.config.Configuration;

/**
 * ZKʵ���ع�����
 * @author xiangqing.tan
 * @version $Id: ZookeeperPoolManager.java, v 0.1 2012-4-1 ����05:17:07 xiangqing.tan Exp $
 */
public class ZookeeperPoolManager {
    private static final Logger           LOGGER = Logger.getLogger(ZookeeperPoolManager.class);

    /** ���� */
    protected static ZookeeperPoolManager instance;

    private ObjectPool<ZooKeeper>         pool;

    /**
     * ���췽��
     */
    protected ZookeeperPoolManager() {
    }

    /**
     * ���ص����Ķ���
     * 
     * @return
     */
    public static ZookeeperPoolManager getInstance() {
        if (instance == null) {
            instance = new ZookeeperPoolManager();
        }
        return instance;
    }

    /**
     * ��ʼ������
     * 
     * @param config
     */
    public void init(Configuration config) {
        PoolableObjectFactory<ZooKeeper> factory = new ZookeeperPoolableObjectFactory(config);

        //��ʼ��ZK�����
        int maxIdle = NumberUtils.toInt(config.getString(Configuration.MAX_IDLE));
        int initIdleCapacity = NumberUtils
            .toInt(config.getString(Configuration.INIT_IDLE_CAPACITY));
        pool = new StackObjectPool<ZooKeeper>(factory, maxIdle, initIdleCapacity);
        //��ʼ����
        for (int i = 0; i < initIdleCapacity; i++) {
            try {
                pool.addObject();
            } catch (IllegalStateException ex) {
                LOGGER.error("��ʼ���ط����쳣��", ex);
            } catch (UnsupportedOperationException ex) {
                LOGGER.error("��ʼ���ط����쳣��", ex);
            } catch (Exception ex) {
                LOGGER.error("��ʼ���ط����쳣��", ex);
            }
        }
    }

    /**
     * ��ZK����Ӷ������ȡ��
     * 
     * @return
     */
    public ZooKeeper borrowObject() {
        if (pool != null) {
            try {
                ZooKeeper zk = pool.borrowObject();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("��ZK������з���ʵ����zk.sessionId=" + zk.getSessionId());
                }
                return zk;
            } catch (NoSuchElementException ex) {
                LOGGER.error("����ZK�ػ�ʵ��ʱ�����쳣��", ex);
            } catch (IllegalStateException ex) {
                LOGGER.error("����ZK�ػ�ʵ��ʱ�����쳣��", ex);
            } catch (Exception e) {
                LOGGER.error("����ZK�ػ�ʵ��ʱ�����쳣��", e);
            }
        }
        return null;
    }

    /**
     * ��ZKʵ�����ض����
     * 
     * @param zk
     */
    public void returnObject(ZooKeeper zk) {
        if (pool != null && zk != null) {
            try {
                pool.returnObject(zk);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("��ZKʵ�����ض�����У�zk.sessionId=" + zk.getSessionId());
                }
            } catch (Exception ex) {
                LOGGER.error("����ZK�ػ�ʵ��ʱ�����쳣��", ex);
            }
        }
    }

    /**
     * �رն����
     */
    public void close() {
        if (pool != null) {
            try {
                pool.close();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("�ر�ZK��������");
                }
            } catch (Exception ex) {
                LOGGER.error("�ر�ZK�����ʱ�����쳣��", ex);
            }
        }
    }
}
