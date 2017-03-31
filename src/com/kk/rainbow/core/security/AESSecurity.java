package com.kk.rainbow.core.security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.kk.rainbow.core.exception.RException;
import com.sun.crypto.provider.SunJCE;

public class AESSecurity
  implements ISecurity
{
  private static String defaultKey = "7s8F0T3G2H8V6F4j";
  private static int keyLength = 16;
  private Cipher encryptCipher = null;
  private Cipher decryptCipher = null;

  public AESSecurity()
    throws RException
  {
    this(defaultKey);
  }

  public AESSecurity(String paramString)
    throws RException
  {
    Security.addProvider(new SunJCE());
    Key localKey = getKey(paramString.getBytes());
    try
    {
      this.encryptCipher = Cipher.getInstance("AES");
      this.encryptCipher.init(1, localKey);
      this.decryptCipher = Cipher.getInstance("AES");
      this.decryptCipher.init(2, localKey);
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new RException("-1", localNoSuchAlgorithmException.getMessage());
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new RException("-1", localNoSuchPaddingException.getMessage());
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw new RException("-1", localInvalidKeyException.getMessage());
    }
  }

  public byte[] encrypt(byte[] paramArrayOfByte)
    throws RException
  {
    try
    {
      return this.encryptCipher.doFinal(paramArrayOfByte);
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
      throw new RException("-1", localIllegalBlockSizeException.getMessage());
    }
    catch (BadPaddingException localBadPaddingException)
    {
      throw new RException("-1", localBadPaddingException.getMessage());
    }
  }

  public String encrypt(String paramString)
    throws RException
  {
    return byteArr2HexStr(encrypt(paramString.getBytes()));
  }

  public byte[] decrypt(byte[] paramArrayOfByte)
    throws RException
  {
    try
    {
      return this.decryptCipher.doFinal(paramArrayOfByte);
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
      throw new RException("-1", localIllegalBlockSizeException.getMessage());
    }
    catch (BadPaddingException localBadPaddingException)
    {
      throw new RException("-1", localBadPaddingException.getMessage());
    }
  }

  public String decrypt(String paramString)
    throws RException
  {
    return new String(decrypt(hexStr2ByteArr(paramString)));
  }

  private Key getKey(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[keyLength];
    for (int i = 0; (i < paramArrayOfByte.length) && (i < arrayOfByte.length); i++)
      arrayOfByte[i] = paramArrayOfByte[i];
    return new SecretKeySpec(arrayOfByte, "AES");
  }

  public static String byteArr2HexStr(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    StringBuffer localStringBuffer = new StringBuffer(i * 2);
    for (int j = 0; j < i; j++)
    {
      int k = paramArrayOfByte[j];
      while (k < 0)
        k += 256;
      if (k < 16)
        localStringBuffer.append("0");
      localStringBuffer.append(Integer.toString(k, 16));
    }
    return localStringBuffer.toString();
  }

  public static byte[] hexStr2ByteArr(String paramString)
  {
    byte[] arrayOfByte1 = paramString.getBytes();
    int i = arrayOfByte1.length;
    byte[] arrayOfByte2 = new byte[i / 2];
    int j = 0;
    while (j < i)
    {
      String str = new String(arrayOfByte1, j, 2);
      arrayOfByte2[(j / 2)] = ((byte)Integer.parseInt(str, 16));
      j += 2;
    }
    return arrayOfByte2;
  }
}