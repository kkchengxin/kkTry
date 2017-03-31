package com.kk.rainbow.core.module.mail;

import java.io.File;
import java.util.Map;

public abstract interface ISendMail {
	public abstract boolean sendText(String paramString1, String paramString2,
			String paramString3, String[] paramArrayOfString1,
			String[] paramArrayOfString2, File[] paramArrayOfFile);

	public abstract boolean sendText(String paramString1, String paramString2,
			String paramString3, String[] paramArrayOfString1,
			String[] paramArrayOfString2, Map paramMap);

	public abstract boolean[] sendText(String paramString1,
			String paramString2, String[] paramArrayOfString1,
			String[] paramArrayOfString2, String[] paramArrayOfString3,
			File[] paramArrayOfFile);

	public abstract boolean[] sendText(String paramString1,
			String paramString2, String[] paramArrayOfString1,
			String[] paramArrayOfString2, String[] paramArrayOfString3,
			Map paramMap);

	public abstract boolean sendHtml(String paramString1, String paramString2,
			String paramString3, String[] paramArrayOfString1,
			String[] paramArrayOfString2, File[] paramArrayOfFile);

	public abstract boolean sendHtml(String paramString1, String paramString2,
			String paramString3, String[] paramArrayOfString1,
			String[] paramArrayOfString2, Map paramMap);

	public abstract boolean[] sendHtml(String paramString1,
			String paramString2, String[] paramArrayOfString1,
			String[] paramArrayOfString2, String[] paramArrayOfString3,
			File[] paramArrayOfFile);

	public abstract boolean[] sendHtml(String paramString1,
			String paramString2, String[] paramArrayOfString1,
			String[] paramArrayOfString2, String[] paramArrayOfString3,
			Map paramMap);
}