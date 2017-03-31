/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.storevm.toolkits.session.metadata.SessionMetaData;
import org.storevm.toolkits.utils.SerializationUtils;

/**
 * ��ʱ������
 * @author xiangqing.tan
 * @version $Id: TimeoutHandler.java, v 0.1 2012-4-9 ����09:44:31 xiangqing.tan Exp $
 */
public class TimeoutHandler extends GetMetadataHandler {

    /**
     * @param id
     */
    public TimeoutHandler(String id) {
        super(id);
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.ZookeeperHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;
            //��ȡԪ����
            SessionMetaData metadata = super.handle();
            //��������ڣ���ֱ�ӷ���true
            if (metadata == null) {
                return (T) Boolean.TRUE;
            }
            //����ڵ�ʧЧ��ֱ�ӷ���
            if (!metadata.getValidate()) {
                return (T) Boolean.TRUE;
            } else {
                //���ڵ��Ƿ�ʱ
                Long now = System.currentTimeMillis(); //��ǰʱ��
                //����Ƿ����
                Long timeout = metadata.getLastAccessTm() + metadata.getMaxIdle(); //����ʱ��
                //�������ʱ��С�ڵ�ǰʱ�䣬���ʾSession��ʱ
                if (timeout < now) {
                    metadata.setValidate(false);
                    //���½ڵ�����
                    byte[] data = SerializationUtils.serialize(metadata);
                    zookeeper.setData(path, data, metadata.getVersion());
                }
                String timeoutStr = DateFormatUtils.format(timeout, "yyyy-MM-dd HH:mm");
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("session��ʱ���:[" + timeoutStr + "]");
                }
            }
        }
        return (T) Boolean.FALSE;
    }

}
