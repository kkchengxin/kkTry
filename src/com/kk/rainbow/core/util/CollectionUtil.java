package com.kk.rainbow.core.util;

import java.io.PrintStream;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CollectionUtil
{
  public static String toString(Collection<?> paramCollection)
  {
    Pattern localPattern = Pattern.compile("\\[|\\]");
    Matcher localMatcher = localPattern.matcher(paramCollection.toString());
    String str = localMatcher.replaceAll("");
    localPattern = Pattern.compile(", ");
    localMatcher = localPattern.matcher(str);
    return localMatcher.replaceAll(",");
  }

  public static void main(String[] paramArrayOfString)
  {
    String str = " 1.2";
    Pattern localPattern = Pattern.compile("[0-9.]+");
    Matcher localMatcher = localPattern.matcher(str.trim());
    boolean bool = localMatcher.matches();
    System.out.println(bool);
  }
}