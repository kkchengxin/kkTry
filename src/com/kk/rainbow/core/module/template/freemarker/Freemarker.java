package com.kk.rainbow.core.module.template.freemarker;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kk.rainbow.core.manager.Log;
import com.kk.rainbow.core.module.template.AbstractTemplate;
import com.kk.rainbow.core.util.FileUtil;
import com.kk.rainbow.core.util.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Freemarker extends AbstractTemplate {
	public String getTargetCode(String paramString, Object paramObject) {
		try {
			return doCore(paramString, paramObject);
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} catch (TemplateException localTemplateException) {
			localTemplateException.printStackTrace();
		}
		return null;
	}

	public boolean privew(String paramString, Object paramObject,
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse) {
		try {
			doCore(paramString, paramObject, null, null,
					paramHttpServletRequest, paramHttpServletResponse, 1);
			return true;
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} catch (TemplateException localTemplateException) {
			localTemplateException.printStackTrace();
		}
		return false;
	}

	public boolean privew(String paramString1, Object paramObject,
			String paramString2, HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse) {
		try {
			doCore(paramString1, paramObject, paramString2, null,
					paramHttpServletRequest, paramHttpServletResponse, 1);
			return true;
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} catch (TemplateException localTemplateException) {
			localTemplateException.printStackTrace();
		}
		return false;
	}

	public String process(String paramString1, Object paramObject,
			String paramString2, String paramString3) {
		try {
			String str = createPathFilename(paramString2, paramString3);
			doCore(paramString1, paramObject, null, str, null, null, 2);
			return str;
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} catch (TemplateException localTemplateException) {
			localTemplateException.printStackTrace();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public String process(String paramString1, Object paramObject,
			String paramString2, String paramString3, String paramString4) {
		try {
			String str = createPathFilename(paramString3, paramString4);
			doCore(paramString1, paramObject, paramString2, str, null, null, 2);
			return str;
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} catch (TemplateException localTemplateException) {
			localTemplateException.printStackTrace();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	private String createPathFilename(String paramString1, String paramString2)
			throws Exception {
		if ((paramString1 == null) || (paramString1.isEmpty()))
			return null;
		paramString1 = (paramString1.endsWith(File.separator))
				|| (paramString1.endsWith("/")) ? paramString1
				: new StringBuilder().append(paramString1)
						.append(File.separator).toString();
		File localFile = new File(paramString1);
		if ((!localFile.isDirectory()) && (!localFile.mkdirs()))
			throw new Exception("Create to-path is failed.");
		if ((paramString2 == null) || (paramString2.isEmpty()))
			return new StringBuilder().append(paramString1)
					.append(FileUtil.getFilenameByCurrtime("yyyyMMdd-HHmmSSS"))
					.append(".").append("html").toString();
		return new StringBuilder().append(paramString1).append(paramString2)
				.toString();
	}

	private void doCore(String paramString1, Object paramObject,
			String paramString2, String paramString3,
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, int paramInt)
			throws ClassNotFoundException, IOException, TemplateException {
		if ((paramString1 == null) || (paramString1.isEmpty()))
			throw new NullPointerException("TemplateFilename is NUll.");
		if ((paramInt != 1)
				&& ((paramString3 == null) || (paramString3.isEmpty())))
			throw new NullPointerException("ToPathFilename is NUll.");
		Configuration localConfiguration = null;
		if (StringUtil.isEmpty(this.templateRootPath)) {
			Log.info("||You don't provide templateRootPath, auto default templateRootPath \"classpath:/tmpl\"");
			localConfiguration = ConfigUtil.getInstance().getConfiguration(
					getClass(), "/tmpl");
		} else {
			Log.info(new StringBuilder()
					.append("||You has provided templateRootPath,")
					.append(this.templateRootPath).toString());
			localConfiguration = ConfigUtil.getInstance().getConfiguration(
					this.templateRootPath);
		}
		Template localTemplate = localConfiguration.getTemplate(paramString1);
		PrintWriter localPrintWriter = null;
		BufferedWriter localBufferedWriter = null;
		try {
			if (paramInt == 2) {
				System.out.println(new StringBuilder()
						.append("create the path+filename is :")
						.append(paramString3).toString());
				localBufferedWriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(
								paramString3), "utf-8"));
				localTemplate.process(paramObject, localBufferedWriter);
			} else {
				paramHttpServletResponse.setContentType(new StringBuilder()
						.append("text/html;charset=")
						.append(paramString2 == null ? "UTF-8" : paramString2)
						.toString());
				localPrintWriter = paramHttpServletResponse.getWriter();
				localTemplate.process(paramObject, localPrintWriter);
			}
		} finally {
			if (localPrintWriter != null) {
				localPrintWriter.flush();
				localPrintWriter.close();
			}
			if (localBufferedWriter != null) {
				localBufferedWriter.flush();
				localBufferedWriter.close();
			}
		}
	}

	private String doCore(String paramString, Object paramObject)
			throws ClassNotFoundException, IOException, TemplateException {
		OutputStreamWriter localOutputStreamWriter = null;
		try {
			Configuration localConfiguration = ConfigUtil.getInstance()
					.getConfiguration(getClass(), "/tmpl");
			Template localTemplate = localConfiguration
					.getTemplate(paramString);
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localOutputStreamWriter = new OutputStreamWriter(
					localByteArrayOutputStream);
			localTemplate.process(paramObject, localOutputStreamWriter);
			String str = localByteArrayOutputStream.toString();
			return str;
		} finally {
			if (localOutputStreamWriter != null)
				localOutputStreamWriter.close();
		}
	}
}