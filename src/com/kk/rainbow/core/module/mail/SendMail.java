package com.kk.rainbow.core.module.mail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.kk.rainbow.core.util.FileUtil;

public class SendMail {
	JavaMailSenderImpl mailImpl = new JavaMailSenderImpl();
	MimeMessage message = null;
	MimeMessageHelper helper = null;
	private String host;
	private String port;
	private String username;
	private String password;
	private String encoding;
	private String isauth;
	private String from;

	public SendMail() throws IOException {
		initConfig();
	}

	public void send(String paramString1, String paramString2, List paramList1,
			List paramList2, List paramList3, List paramList4)
			throws MessagingException, UnsupportedEncodingException {
		this.mailImpl.setHost(this.host);
		this.mailImpl.setPort(this.port == null ? 25 : Integer
				.parseInt(this.port));
		this.mailImpl.setUsername(this.username);
		this.mailImpl.setPassword(this.password);
		this.mailImpl.setDefaultEncoding(this.encoding);
		Properties localProperties = new Properties();
		localProperties.setProperty("mail.smtp.auth", this.isauth);
		this.mailImpl.setJavaMailProperties(localProperties);
		this.message = this.mailImpl.createMimeMessage();
		this.helper = new MimeMessageHelper(this.message, true, this.encoding);
		this.message.setSubject(paramString1);
		this.helper.setFrom(this.from);
		if ((paramList1 != null) && (!paramList1.isEmpty()))
			this.helper.setTo((String[]) paramList1
					.toArray(new String[paramList1.size()]));
		else
			System.out.println("SendMail class : toAddress is Null !");
		if ((paramList2 != null) && (!paramList2.isEmpty()))
			this.helper.setCc((String[]) paramList2
					.toArray(new String[paramList2.size()]));
		else
			System.out.println("SendMail class : ccAddress is Null !");
		if ((paramList3 != null) && (!paramList3.isEmpty()))
			this.helper.setBcc((String[]) paramList3
					.toArray(new String[paramList3.size()]));
		else
			System.out.println("SendMail class : bccAddress is Null !");
		if ((paramList4 != null) && (!paramList4.isEmpty())) {
			Iterator localIterator = paramList4.iterator();
			File localFile = null;
			String str = null;
			while (localIterator.hasNext()) {
				str = (String) localIterator.next();
				if (str != null) {
					localFile = new File(str);
					this.helper.addAttachment(MimeUtility.encodeWord(
							localFile.getName(), "UTF-8", "GBK"), localFile);
				}
			}
		}
		this.helper.setText(paramString2, true);
		this.mailImpl.send(this.message);
	}

	public void send(String paramString1, String paramString2, List paramList1,
			List paramList2, List paramList3, Map paramMap)
			throws MessagingException, UnsupportedEncodingException {
		this.mailImpl.setHost(this.host);
		this.mailImpl.setPort(this.port == null ? 25 : Integer
				.parseInt(this.port));
		this.mailImpl.setUsername(this.username);
		this.mailImpl.setPassword(this.password);
		this.mailImpl.setDefaultEncoding(this.encoding);
		Properties localProperties = new Properties();
		localProperties.setProperty("mail.smtp.auth", this.isauth);
		this.mailImpl.setJavaMailProperties(localProperties);
		this.message = this.mailImpl.createMimeMessage();
		this.helper = new MimeMessageHelper(this.message, true, this.encoding);
		this.helper.setSubject(paramString1);
		this.helper.setFrom(this.from);
		if ((paramList1 != null) && (!paramList1.isEmpty()))
			this.helper.setTo((String[]) paramList1
					.toArray(new String[paramList1.size()]));
		else
			System.out.println("SendMail class : toAddress is Null !");
		if ((paramList2 != null) && (!paramList2.isEmpty()))
			this.helper.setCc((String[]) paramList2
					.toArray(new String[paramList2.size()]));
		else
			System.out.println("SendMail class : ccAddress is Null !");
		if ((paramList3 != null) && (!paramList3.isEmpty()))
			this.helper.setBcc((String[]) paramList3
					.toArray(new String[paramList3.size()]));
		else
			System.out.println("SendMail class : bccAddress is Null !");
		this.helper.setText(paramString2, true);
		if ((paramMap != null) && (!paramMap.isEmpty())) {
			Set localSet = paramMap.keySet();
			Iterator localIterator = localSet.iterator();
			String str1 = null;
			String str2 = null;
			File localFile = null;
			while (localIterator.hasNext()) {
				str1 = (String) localIterator.next();
				str2 = (String) paramMap.get(str1);
				localFile = new File(str2);
				if (str2 != null)
					this.helper.addAttachment(
							MimeUtility.encodeWord(str1
									+ FileUtil.getFix(localFile)), localFile);
			}
		}
		this.mailImpl.send(this.message);
	}

	private void initConfig() throws IOException {
		Properties localProperties = new Properties();
		BufferedInputStream localBufferedInputStream = null;
		try {
			localBufferedInputStream = new BufferedInputStream(getClass()
					.getClassLoader().getResourceAsStream(
							"rainbow_mail.properties"));
			localProperties.load(localBufferedInputStream);
			this.host = localProperties.getProperty("mail.stmp.host");
			this.port = localProperties.getProperty("mail.stmp.port");
			this.username = localProperties.getProperty("mail.username");
			this.password = localProperties.getProperty("mail.password");
			this.encoding = localProperties.getProperty("mail.encoding");
			this.isauth = localProperties.getProperty("mail.smtp.auth");
			this.from = localProperties.getProperty("mail.from");
			System.out
					.println("Mail--read the config of the rainbow_mail.properties! below:\n");
			System.out.println("Mail config------start----------------");
			System.out.println("host--" + this.host);
			System.out.println("password--" + this.password);
			System.out.println("username--" + this.username);
			System.out.println("password--" + this.password);
			System.out.println("encoding--" + this.encoding);
			System.out.println("isauth--" + this.isauth);
			System.out.println("from--" + this.from);
			System.out.println("Mail config------end----------------");
		} finally {
			localProperties.clear();
			if (localBufferedInputStream != null)
				localBufferedInputStream.close();
		}
	}
}