/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

import org.storevm.toolkits.session.metadata.SessionMetaData;

/**
 * ���ؽڵ��ֵ���Ƽ��ϵĴ�����
 * @author xiangqing.tan
 * @version $Id: GetNodeNames.java, v 0.1 2012-4-9 ����12:10:56 xiangqing.tan Exp $
 */
public class GetNodeNames extends GetMetadataHandler {

    /**
     * @param id
     */
    public GetNodeNames(String id) {
        super(id);
    }

    /** 
     * @see org.storevm.toolkits.session.zookeeper.handler.GetMetadataHandler#handle()
     */
    @Override
    public <T> T handle() throws Exception {
        if (zookeeper != null) {
            String path = GROUP_NAME + NODE_SEP + id;

            //��ȡԪ����
            SessionMetaData metadata = super.handle();
            //��������ڻ�����Ч����ֱ�ӷ���null
            if (metadata == null || !metadata.getValidate()) {
                return null;
            }
            //��ȡ�����ӽڵ�
            return (T) zookeeper.getChildren(path, false);
        }
        return (T) null;
    }

}
