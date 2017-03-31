/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import org.apache.zookeeper.data.Stat;
import org.storevm.toolkits.session.zookeeper.AbstractZookeeperHandler;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * ����ָ����ֵ�ڵ����ݵĴ�����
 * @author xiangqing.tan
 * @version $Id: GetDataHandler.java, v 0.1 2012-4-9 ����09:59:56 xiangqing.tan Exp $
 */
public class GetDataHandler extends AbstractZookeeperHandler {
    /** ��ֵ���� */
    protected String key;

    /**
     * @param id
     */
    protected GetDataHandler(String id) {
        super(id);
    }

    /**
     * ���췽��
     * @param id
     * @param key
     */
    public GetDataHandler(String id, String key) {
        this(id);
        this.key = key;
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.ZookeeperHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;
            // ���ָ����Session�ڵ��Ƿ����
            Stat stat = zookeeper.exists(path, false);
            if (stat != null) {
                //�������ݽڵ��Ƿ����
                String dataPath = path + NODE_SEP + key;
                stat = zookeeper.exists(dataPath, false);
                Object obj = null;
                if (stat != null) {
                    //��ȡ�ڵ�����
                    byte[] data = zookeeper.getData(dataPath, false, null);
                    if (data != null) {
                        //�����л�
                        obj = SerializationUtils.deserialize(data);
                    }
                }
                return (T) obj;
            }
        }
        return (T) null;
    }

}
