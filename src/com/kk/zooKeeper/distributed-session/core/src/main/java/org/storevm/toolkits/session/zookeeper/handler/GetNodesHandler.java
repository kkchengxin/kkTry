/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.data.Stat;
import org.storevm.toolkits.session.metadata.SessionMetaData;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * �������нڵ�Map�Ĵ�����
 * @author xiangqing.tan
 * @version $Id: GetNodesHandler.java, v 0.1 2012-4-9 ����10:52:37 xiangqing.tan Exp $
 */
public class GetNodesHandler extends GetMetadataHandler {

    /**
     * @param id
     */
    public GetNodesHandler(String id) {
        super(id);
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.ZookeeperHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        Map<String, Object> nodeMap = new HashMap<String, Object>();
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;

            //��ȡԪ����
            SessionMetaData metadata = super.handle();
            //��������ڻ�����Ч����ֱ�ӷ���null
            if (metadata == null || !metadata.getValidate()) {
                return null;
            }
            //��ȡ�����ӽڵ�
            List<String> nodes = zookeeper.getChildren(path, false);
            //�������
            for (String node : nodes) {
                String dataPath = path + NODE_SEP + node;
                Stat stat = zookeeper.exists(dataPath, false);
                //�ڵ����
                if (stat != null) {
                    //��ȡ����
                    byte[] data = zookeeper.getData(dataPath, false, null);
                    if (data != null) {
                        nodeMap.put(node, SerializationUtils.deserialize(data));
                    } else {
                        nodeMap.put(node, null);
                    }
                }
            }
        }
        return (T) nodeMap;
    }

}
