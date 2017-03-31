/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import org.apache.zookeeper.data.Stat;
import org.storevm.toolkits.session.metadata.SessionMetaData;
import org.storevm.toolkits.session.zookeeper.AbstractZookeeperHandler;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * ����Ԫ���ݵĴ�����ʵ��
 * @author Administrator
 * @version $Id: GetMetadataHandler.java, v 0.1 2012-4-8 ����7:10:47 Administrator Exp $
 */
public class GetMetadataHandler extends AbstractZookeeperHandler {
    /**
     * ���췽��
     * @param nodeId
     */
    public GetMetadataHandler(String id) {
        super(id);
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.ZookeeperHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;
            // ���ڵ��Ƿ����
            Stat stat = zookeeper.exists(path, false);
            //statΪnull��ʾ�޴˽ڵ�
            if (stat == null) {
                return null;
            }
            //��ȡ�ڵ��ϵ�����
            byte[] data = zookeeper.getData(path, false, null);
            if (data != null) {
                //�����л�
                Object obj = SerializationUtils.deserialize(data);
                //ת������
                if (obj instanceof SessionMetaData) {
                    SessionMetaData metadata = (SessionMetaData) obj;
                    //���õ�ǰ�汾��
                    metadata.setVersion(stat.getVersion());
                    return (T) metadata;
                }
            }
        }
        return (T) null;
    }
}
