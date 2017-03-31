package com.kk.rainbow.core.attach;

import java.io.File;

import com.kk.rainbow.core.util.StringUtil;
import com.oreilly.servlet.multipart.FileRenamePolicy;

public class FileNamePolicy
  implements FileRenamePolicy
{
  private String saveDir;
  private String saveFileName;

  public FileNamePolicy(String paramString)
  {
    this.saveFileName = paramString;
  }

  public FileNamePolicy(String paramString1, String paramString2)
  {
    this.saveDir = paramString1;
    this.saveFileName = paramString2;
  }

  public File rename(File paramFile)
  {
    if (StringUtil.isEmpty(this.saveDir))
      this.saveDir = paramFile.getParent();
    File localFile = new File(this.saveDir, this.saveFileName);
    boolean bool = paramFile.renameTo(localFile);
    if (!bool)
      paramFile.deleteOnExit();
    paramFile = null;
    return localFile;
  }

  public static void main(String[] paramArrayOfString)
  {
    File localFile1 = new File("d:/aa.properties");
    File localFile2 = new FileNamePolicy("aaa1111.properties").rename(localFile1);
    System.out.println(localFile1.getAbsolutePath());
    System.out.println(localFile1.getParent());
    System.out.println(localFile1.getParentFile().getAbsolutePath());
    System.out.println(localFile2.getPath() + ":" + localFile2.getName());
  }
}