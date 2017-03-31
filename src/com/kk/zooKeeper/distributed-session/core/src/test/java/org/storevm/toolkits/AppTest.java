package org.storevm.toolkits;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.storevm.toolkits.session.config.Configuration;
import org.storevm.toolkits.session.pool.ZookeeperPoolManager;
import org.storevm.toolkits.session.zookeeper.DefaultZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.ZooKeeperClient;
import org.storevm.toolkits.session.zookeeper.handler.CreateGroupNodeHandler;
import org.storevm.toolkits.session.zookeeper.handler.CreateNodeHandler;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception {
        final String GROUP_NAME = "/SESSIONS";

        //��ȡ������Ϣ����
        Configuration config = Configuration.getInstance();

        //��ʼ��ZKʵ����
        ZookeeperPoolManager.getInstance().init(config);

        //��ȡZK�ͻ���
        ZooKeeperClient client = DefaultZooKeeperClient.getInstance();

        long current = System.currentTimeMillis();
        client.execute(new CreateGroupNodeHandler());
        System.out.println("create group node success");

        String id = GROUP_NAME + "/" + "xxxxxxxx001";
        client.execute(new CreateNodeHandler(id, null));
        System.out.println("create node id:" + id);

        System.out.println("exec time:[" + (System.currentTimeMillis() - current) + "]");

        //�ر�ZKʵ����
        ZookeeperPoolManager.getInstance().close();
    }
}
