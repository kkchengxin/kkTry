package com.kk.rainbow.core.ceptor.self;

import java.util.List;

public abstract interface ITest
{
  public abstract List getList();

  public abstract List getList(int paramInt1, int paramInt2, int paramInt3);

  public abstract boolean addtopic(List paramList);
}