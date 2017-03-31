/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.data.Stat;
import org.storevm.toolkits.session.zookeeper.AbstractZookeeperHandler;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * ɾ���ڵ�Ĵ�����
 * @author xiangqing.tan
 * @version $Id: RemoveNodeHandler.java, v 0.1 2012-4-9 ����10:44:39 xiangqing.tan Exp $
 */
public class RemoveNodeHandler extends AbstractZookeeperHandler {
    /**
     * ���췽��
     * @param id
     */
    public RemoveNodeHandler(String id) {
        super(id);
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.ZookeeperHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        Map<String, Serializable> datas = new HashMap<String, Serializable>();
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;
            // ���ڵ��Ƿ����
            Stat stat = zookeeper.exists(path, false);
            //����ڵ������ɾ��֮
            if (stat != null) {
                //��ɾ���ӽڵ�
                List<String> nodes = zookeeper.getChildren(path, false);
                if (nodes != null) {
                    for (String node : nodes) {
                        String dataPath = path + "/" + node;
                        //��ȡ����
                        byte[] data = zookeeper.getData(dataPath, false, null);
                        if (data != null) {
                            //�����л�
                            Object obj = SerializationUtils.deserialize(data);
                            datas.put(node, (Serializable) obj);
                        }
                        zookeeper.delete(dataPath, -1);
                    }
                }
                //ɾ�����ڵ�
                zookeeper.delete(path, -1);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("ɾ��Session�ڵ����:[" + path + "]");
                }
            }
        }
        return (T) datas;
    }

}
