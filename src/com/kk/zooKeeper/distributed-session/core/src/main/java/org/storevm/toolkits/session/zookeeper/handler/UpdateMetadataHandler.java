/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import org.apache.zookeeper.ZooKeeper;
import org.storevm.toolkits.session.metadata.SessionMetaData;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * ����Ԫ���ݵĴ�����
 * @author xiangqing.tan
 * @version $Id: UpdateMetaDataHandler.java, v 0.1 2012-4-9 ����09:31:10 xiangqing.tan Exp $
 */
public class UpdateMetadataHandler extends GetMetadataHandler {

    /**
     * @param nodeId
     */
    public UpdateMetadataHandler(String id) {
        super(id);
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.handler.GetMetadataHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        if (zookeeper != null) {
            //��ȡԪ����
            SessionMetaData metadata = super.handle();
            if (metadata != null) {
                updateMetadata(metadata, zookeeper);
                return (T) metadata.getValidate();
            }
        }
        return (T) Boolean.FALSE;
    }

    /**
     * ���½ڵ�����
     * 
     * @param metadata
     * @param zk
     * @throws Exception
     */
    protected void updateMetadata(SessionMetaData metadata, ZooKeeper zk) throws Exception {
        if (metadata != null) {
            String id = metadata.getId();
            Long now = System.currentTimeMillis(); //��ǰʱ��
            //����Ƿ����
            Long timeout = metadata.getLastAccessTm() + metadata.getMaxIdle(); //����ʱ��
            //�������ʱ��С�ڵ�ǰʱ�䣬���ʾSession��ʱ
            if (timeout < now) {
                metadata.setValidate(false);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Session�ڵ��ѳ�ʱ[" + id + "]");
                }
            }
            //�������һ�η���ʱ��
            metadata.setLastAccessTm(now);
            //���½ڵ�����
            String path = GROUP_NAME + NODE_SEP + id;
            byte[] data = SerializationUtils.serialize(metadata);
            zk.setData(path, data, metadata.getVersion());
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("����Session�ڵ��Ԫ�������[" + path + "]");
            }
        }
    }
}
