package com.kk.rainbow.core.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

import com.kk.rainbow.core.exception.RException;
import com.kk.rainbow.core.manager.Log;
import com.kk.rainbow.core.util.HttpClientUtil;

public class ZIPUtil {
	public static final boolean decompressZIP(String paramString1,
			Map<String, String> paramMap, String paramString2,
			String paramString3) throws RException {
		return new HttpClientUtil().decompressZIP(paramString1, paramMap,
				paramString2, paramString3);
	}

	public static boolean decompressZIP(File paramFile, String paramString1,
			String paramString2) throws RException {
		boolean bool = false;
		if (paramString1 == null)
			paramString1 = "zip";
		try {
			ArchiveInputStream localArchiveInputStream = new ArchiveStreamFactory()
					.createArchiveInputStream(paramString1,
							new GZIPInputStream(new FileInputStream(paramFile)));
			ArchiveEntry localArchiveEntry;
			while ((localArchiveEntry = localArchiveInputStream.getNextEntry()) != null) {
				File localFile = new File(paramString2,
						localArchiveEntry.getName());
				if (localArchiveEntry.isDirectory()) {
					if (!localFile.exists())
						localFile.mkdirs();
				} else {
					Log.info("localhost file len==>" + localFile.length());
					Log.info("remote file len==>" + localArchiveEntry.getSize());
					if ((!localFile.exists())
							|| (localArchiveEntry.getSize() != localFile
									.length())) {
						FileOutputStream localFileOutputStream = new FileOutputStream(
								localFile);
						IOUtils.copy(localArchiveInputStream,
								localFileOutputStream);
						localFileOutputStream.flush();
						localFileOutputStream.close();
					}
				}
			}
			if (localArchiveInputStream != null)
				localArchiveInputStream.close();
			bool = true;
		} catch (ArchiveException localArchiveException) {
			localArchiveException.printStackTrace();
			throw new RException("-1", "The source resource is not ZIP file");
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			throw new RException(
					"-1",
					"Thre source stream is not varaiable or The folder is not exist or The file is destroyed");
		}
		return bool;
	}

	public static void main(String[] paramArrayOfString) throws RException {
		File localFile = new File("D:\\data\\ASC\\all_20111114.tar.gz");
		String str1 = "tar";
		String str2 = "D:\\data\\ASC";
		long l = System.currentTimeMillis();
		System.out.println(l);
		decompressZIP(localFile, str1, str2);
		System.out.println(System.currentTimeMillis() - l);
	}
}