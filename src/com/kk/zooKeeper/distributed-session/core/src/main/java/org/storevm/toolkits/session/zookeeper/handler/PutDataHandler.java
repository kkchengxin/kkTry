/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import java.io.Serializable;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * �����ݷ���ڵ�Ĵ�����
 * @author xiangqing.tan
 * @version $Id: PutDataHandler.java, v 0.1 2012-4-9 ����10:22:28 xiangqing.tan Exp $
 */
public class PutDataHandler extends GetDataHandler {
    /** ��Žڵ������ */
    private Serializable data;

    /**
     * 
     * @param id
     * @param key
     * @param data
     */
    public PutDataHandler(String id, String key, Serializable data) {
        super(id, key);
        this.data = data;
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.handler.GetDataHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;
            // ���ָ����Session�ڵ��Ƿ����
            Stat stat = zookeeper.exists(path, false);
            //����ڵ������ɾ��֮
            if (stat != null) {
                //�������ݽڵ��Ƿ���ڣ������ھʹ���һ��
                String dataPath = path + NODE_SEP + key;
                stat = zookeeper.exists(dataPath, false);
                if (stat == null) {
                    //�������ݽڵ�
                    zookeeper.create(dataPath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("�������ݽڵ����[" + dataPath + "]");
                    }
                }
                //�ڽڵ����������ݣ��������ݱ�������л�
                if (data instanceof Serializable) {
                    int dataNodeVer = -1;
                    if (stat != null) {
                        //��¼���ݽڵ�İ汾
                        dataNodeVer = stat.getVersion();
                    }
                    byte[] arrData = SerializationUtils.serialize(data);
                    stat = zookeeper.setData(dataPath, arrData, dataNodeVer);
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("�������ݽڵ��������[" + dataPath + "][" + data + "]");
                    }
                    return (T) Boolean.TRUE;
                }
            }
        }
        return (T) Boolean.FALSE;
    }

}
