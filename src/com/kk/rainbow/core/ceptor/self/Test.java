package com.kk.rainbow.core.ceptor.self;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Test
  implements ITest
{
  public List getList()
  {
    System.out.println("excute getList");
    return new ArrayList();
  }

  public List getList(int paramInt1, int paramInt2, int paramInt3)
  {
    System.out.println("excute getList");
    return new ArrayList();
  }

  public List getList(ITest paramITest)
  {
    System.out.println("execute private");
    return null;
  }

  public boolean addtopic(List paramList)
  {
    System.out.println("add topic");
    return true;
  }

  public static void main(String[] paramArrayOfString)
  {
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    for (int i = 1; i <= 100000; i++)
      localArrayList1.add(Integer.valueOf(i));
    for (int i = 1; i <= 100; i++)
      localArrayList2.add(Integer.valueOf(i));
  }
}