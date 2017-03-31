package com.kk.rainbow.core.security;

import com.kk.rainbow.core.exception.RException;

public abstract interface ISecurityWrapper
{
  public abstract byte[] encrypt(byte[] paramArrayOfByte)
    throws RException;

  public abstract String encrypt(String paramString)
    throws RException;

  public abstract byte[] decrypt(byte[] paramArrayOfByte)
    throws RException;

  public abstract String decrypt(String paramString)
    throws RException;
}