package com.kk.rainbow.core.util;

import java.io.PrintStream;

public class RandomUtil
{
  private static char[] varLowerPool = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
  private static char[] varUpperPool = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
  private static char[] intPool = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

  public static final String genFixcountStr(int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramInt; i++)
    {
      int j = (int)(Math.random() * 3.0D);
      switch (j)
      {
      case 0:
        localStringBuffer.append(getRandomItem(varLowerPool));
        break;
      case 1:
        localStringBuffer.append(getRandomItem(varUpperPool));
        break;
      case 2:
        localStringBuffer.append(getRandomItem(intPool));
      }
    }
    return localStringBuffer.toString();
  }

  public static final String genFixcountNum(int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramInt; i++)
    {
      char c = getRandomItem(intPool);
      if (c == 0)
        i--;
      else
        localStringBuffer.append(c);
    }
    return localStringBuffer.toString();
  }

  private static final char getRandomItem(char[] paramArrayOfChar)
  {
    return paramArrayOfChar[((int)(Math.random() * (paramArrayOfChar.length - 1)))];
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println(genFixcountStr(8));
    System.out.println(genFixcountNum(8));
    System.out.println(getRandomItem(intPool));
  }
}