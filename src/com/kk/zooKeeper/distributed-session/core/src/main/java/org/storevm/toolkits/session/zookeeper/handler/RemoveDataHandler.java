/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import org.apache.zookeeper.data.Stat;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * 
 * @author xiangqing.tan
 * @version $Id: RemoveDataHandler.java, v 0.1 2012-4-9 ����10:49:17 xiangqing.tan Exp $
 */
public class RemoveDataHandler extends GetDataHandler {

    /**
     * @param id
     * @param key
     */
    public RemoveDataHandler(String id, String key) {
        super(id, key);
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.handler.GetDataHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        Object value = null;
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;

            // ���ָ����Session�ڵ��Ƿ����
            Stat stat = zookeeper.exists(path, false);
            if (stat != null) {
                //�������ݽڵ��Ƿ����
                String dataPath = path + NODE_SEP + key;
                stat = zookeeper.exists(dataPath, false);
                if (stat != null) {
                    //�õ����ݽڵ������
                    byte[] data = zookeeper.getData(dataPath, false, null);
                    if (data != null) {
                        //�����л�
                        value = SerializationUtils.deserialize(data);
                    }
                    //ɾ���ڵ�
                    zookeeper.delete(dataPath, -1);
                }
            }
        }
        return (T) value;
    }

}
