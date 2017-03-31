package com.kk.rainbow.core.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;

public class DSAUtil
{
  public static String sign(String paramString1, String paramString2)
  {
    String str = null;
    PrivateKey localPrivateKey = null;
    byte[] arrayOfByte1 = Base64.decodeBase64(paramString2);
    PKCS8EncodedKeySpec localPKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(arrayOfByte1);
    try
    {
      localPrivateKey = KeyFactory.getInstance("DSA").generatePrivate(localPKCS8EncodedKeySpec);
      Signature localSignature = Signature.getInstance("DSA");
      localSignature.initSign(localPrivateKey);
      byte[] arrayOfByte2 = paramString1.getBytes();
      localSignature.update(arrayOfByte2, 0, arrayOfByte2.length);
      byte[] arrayOfByte3 = localSignature.sign();
      str = new String(Base64.encodeBase64(arrayOfByte3));
    }
    catch (Throwable localThrowable)
    {
    }
    return str;
  }

  public static boolean verify(String paramString1, String paramString2, String paramString3)
  {
    boolean bool = false;
    PublicKey localPublicKey = null;
    byte[] arrayOfByte = Base64.decodeBase64(paramString3);
    X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(arrayOfByte);
    try
    {
      localPublicKey = KeyFactory.getInstance("DSA").generatePublic(localX509EncodedKeySpec);
      Signature localSignature = Signature.getInstance("DSA");
      localSignature.initVerify(localPublicKey);
      localSignature.update(paramString1.getBytes());
      bool = localSignature.verify(Base64.decodeBase64(paramString2));
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return bool;
  }
}