/**
 * Storevm.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.storevm.toolkits.session.zookeeper.handler;

/**
 * 
 * @author xiangqing.tan
 * @version $Id: CreateGroupNodeHandler.java, v 0.1 2012-4-9 ����09:27:34 xiangqing.tan Exp $
 */
public class CreateGroupNodeHandler extends CreateNodeHandler {
    /**
     * ���췽��
     */
    public CreateGroupNodeHandler() {
        this(GROUP_NAME);
    }

    /**
     * ���췽��
     * @param nodeName
     */
    protected CreateGroupNodeHandler(String id) {
        super(id, null);
    }

}
