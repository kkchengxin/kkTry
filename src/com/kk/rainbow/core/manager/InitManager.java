package com.kk.rainbow.core.manager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.kk.rainbow.core.manager.module.LogModule;
import com.kk.rainbow.core.manager.module.ProcessModule;
import com.kk.rainbow.core.reflect.EntityReflect;
import com.kk.rainbow.core.util.ProcessXML;

public class InitManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String XPATH = "/modules/";
	private String WEBROOT;
	private String filename;
	private Document document;

	public void init() throws ServletException {
		super.init();
		this.WEBROOT = getServletContext().getRealPath("/");
		Log.info("应用根路径:" + this.WEBROOT);
		Log.info("rainbow初始化服务配置");
		this.filename = (this.WEBROOT + "WEB-INF/conf/" + "modules.xml");
		if (!new File(this.filename).exists()) {
			Log.info("rainbow尚未配置modules.xml");
		} else {
			Log.info("rainbow已检查到modules.xml");
			this.document = ProcessXML.getInstance().getDocument(this.filename);
		}
		initModule(LogModule.getInstance(), "日志服务");
		initModule(ProcessModule.getInstance(), "执行过程服务");
		this.document = null;
	}

	private void initModule(Object paramObject, String paramString) {
		Log.info("-------rainbow配置<" + paramString + ">-------");
		if (this.document == null) {
			this.document = DocumentHelper.createDocument();
			ProcessXML.getInstance().createXML(
					new InitDocument(this.document, paramObject).getDocument(),
					this.filename);
		} else {
			Class localClass = paramObject.getClass();
			String str = "/modules/" + localClass.getSimpleName();
			if (ProcessXML.getInstance().isExistElement(this.document, str)) {
				Log.info("modules.xml中检测到" + paramString + "节点["
						+ localClass.getSimpleName() + "]");
				setMdouleStatus(this.filename, str, paramObject);
			} else {
				Log.info("modules.xml缺少" + paramString + "节点["
						+ localClass.getSimpleName() + "],或文件内容为空");
				ProcessXML.getInstance().createXML(
						ProcessXML.getInstance().addElement(this.document,
								paramObject), this.filename);
			}
		}
	}

	private void setMdouleStatus(String paramString1, String paramString2,
			Object paramObject) {
		Class localClass = paramObject.getClass();
		Field[] arrayOfField = EntityReflect.getInstance().installPojoFields(
				localClass);
		if (arrayOfField != null)
			try {
				for (int i = 0; i < arrayOfField.length; i++)
					if (EntityReflect.getInstance().isValid(localClass,
							arrayOfField[i])) {
						String str2 = paramString2 + "/"
								+ arrayOfField[i].getName();
						if (ProcessXML.getInstance().getElement(this.document,
								str2) == null) {
							Log.info("modules.xml的节点["
									+ localClass.getSimpleName() + "]下，缺少子节点["
									+ arrayOfField[i].getName() + "]");
							String str1 = (String) EntityReflect.getInstance()
									.executeGetMethod(paramObject,
											arrayOfField[i].getName());
							ProcessXML.getInstance().addElement(this.document,
									"/modules//" + localClass.getSimpleName(),
									arrayOfField[i].getName(), str1);
							ProcessXML.getInstance().createXML(this.document,
									paramString1);
						}
						String str1 = ProcessXML.getInstance().getElementText(
								this.document, paramString2,
								arrayOfField[i].getName());
						EntityReflect.getInstance().executeSetMethod(
								paramObject, arrayOfField[i].getName(), str1);
						Log.info("设置内存中[" + localClass.getSimpleName() + "]的"
								+ arrayOfField[i].getName() + "值：" + str1);
					}
			} catch (IllegalAccessException localIllegalAccessException) {
				localIllegalAccessException.printStackTrace();
			} catch (IllegalArgumentException localIllegalArgumentException) {
				localIllegalArgumentException.printStackTrace();
			} catch (NoSuchMethodException localNoSuchMethodException) {
				localNoSuchMethodException.printStackTrace();
			} catch (NoSuchFieldException localNoSuchFieldException) {
				localNoSuchFieldException.printStackTrace();
			} catch (InvocationTargetException localInvocationTargetException) {
				localInvocationTargetException.printStackTrace();
			}
		arrayOfField = null;
	}
}