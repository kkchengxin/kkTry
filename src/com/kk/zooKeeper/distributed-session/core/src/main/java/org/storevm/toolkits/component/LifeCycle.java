/**
 * 
 */
package org.storevm.toolkits.component;

/**
 * ����������ڽӿڶ���
 * 
 * @author ̸����
 * @version $Id: LifeCycle.java, v 0.1 2010-12-29 ����08:50:10 ̸���� Exp $
 */
public interface LifeCycle {
    /**
     * �����������������ĳ�ʼ��
     * @throws Exception
     */
    public void start() throws Exception;

    /**
     * ���ֹͣ���������������
     * @throws Exception
     */
    public void stop() throws Exception;

    /**
     * �Ƿ�������
     * @return
     */
    public boolean isStarted();

    /**
     * �Ƿ���ֹͣ
     * @return
     */
    public boolean isStopped();
}
