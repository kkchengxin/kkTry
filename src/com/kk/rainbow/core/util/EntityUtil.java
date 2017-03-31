package com.kk.rainbow.core.util;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EntityUtil {
	public static final <T> String serialize(T paramT) throws IOException {
		if (paramT == null)
			return null;
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(
				localByteArrayOutputStream);
		localObjectOutputStream.writeObject(paramT);
		return Base64.encode(localByteArrayOutputStream.toByteArray());
	}

	public static final <T extends Serializable> T deSerialize(
			String paramString) throws IOException, Base64DecodingException,
			ClassNotFoundException {
		if (StringUtil.isEmpty(paramString))
			return null;
		ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
				Base64.decode(paramString.getBytes()));
		ObjectInputStream localObjectInputStream = new ObjectInputStream(
				new BufferedInputStream(localByteArrayInputStream));
		return (T) localObjectInputStream.readObject();
	}
}