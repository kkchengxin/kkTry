/**
 * 
 */
package org.storevm.toolkits.session.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * ������Ϣʵ���࣬���ڶ�ȡ������ϵͳ��������
 * 
 * @author Administrator
 * @version $Id: Configuration.java, v 0.1 2012-4-3 ����9:28:53 Administrator Exp $
 */
public class Configuration {
    /** �������Լ�ֵ */
    public static final String   SERVERS            = "servers";
    public static final String   MAX_IDLE           = "maxIdle";
    public static final String   INIT_IDLE_CAPACITY = "initIdleCapacity";
    public static final String   SESSION_TIMEOUT    = "sessionTimeout";
    public static final String   TIMEOUT            = "timeout";
    public static final String   POOLSIZE           = "poolSize";

    /** �����ļ����� */
    public static final String   CFG_NAME           = ".cfg.properties";

    /** �������� */
    private static Configuration instance;

    /** ���������ļ� */
    private Properties           config;

    /**
     * ���췽��
     */
    protected Configuration() {
        this.config = new Properties();

        //���û�Ŀ¼��ȡ�����ļ�
        String basedir = System.getProperty("user.home");
        File file = new File(basedir, CFG_NAME);
        try {
            //����ļ������ڣ��򴴽����ļ�
            boolean exist = file.exists();
            if (!exist) {
                file.createNewFile();
            }

            //��ȡ��������
            this.config.load(new FileInputStream(file));

            //������ò����ڣ���д��Ĭ��ֵ
            if (!exist) {
                this.config.setProperty(SERVERS, "www.storevm.org");
                this.config.setProperty(MAX_IDLE, "8");
                this.config.setProperty(INIT_IDLE_CAPACITY, "4");
                this.config.setProperty(SESSION_TIMEOUT, "5");
                this.config.setProperty(TIMEOUT, "5000");
                this.config.setProperty(POOLSIZE, "5000");
                this.config.store(new FileOutputStream(file), "");
            }
        } catch (Exception ex) {
            //do nothing...
        }
    }

    /**
     * ����ʵ���ķ���
     * 
     * @return
     */
    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    /**
     * ����ָ�����Լ�ֵ��Ӧ��������ֵ���ַ�����ʽ(����Ĭ��ֵ)
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        if (config != null) {
            return config.getProperty(key) != null ? config.getProperty(key) : defaultValue;
        }
        return defaultValue;
    }

    /**
     * ����ָ�����Լ�ֵ��Ӧ��������ֵ���ַ�����ʽ
     * 
     * @param key
     * @return
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Configuration [config=" + config + "]";
    }
}
