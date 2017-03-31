package com.kk.rainbow.core.dao;

import java.io.Serializable;

import com.kk.rainbow.core.exception.RException;

public class Page
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int totalCount;
  private int totalPage;
  private int limit;
  private int offset;
  private int currPage;

  public Page()
  {
    this.totalCount = 0;
    this.totalPage = 0;
    this.currPage = 1;
    this.limit = 10;
    this.offset = ((this.currPage - 1) * this.limit);
  }

  public Page(int paramInt1, int paramInt2)
  {
    this.currPage = (paramInt1 <= 0 ? 1 : paramInt1);
    this.limit = (paramInt2 > 100 ? 100 : paramInt2);
    this.offset = ((paramInt1 - 1) * paramInt2);
  }

  public Page(int paramInt1, int paramInt2, int paramInt3)
  {
    this.currPage = (paramInt1 <= 0 ? 1 : paramInt1);
    this.limit = (paramInt2 > 100 ? 100 : paramInt2);
    this.offset = ((paramInt1 - 1) * paramInt2);
    this.totalCount = paramInt3;
    this.totalPage = (paramInt3 % paramInt2 > 0 ? Math.abs(paramInt3 / paramInt2) + 1 : paramInt3 / paramInt2);
  }

  public int getTotalCount()
  {
    return this.totalCount;
  }

  public void setTotalCount(int paramInt)
  {
    this.totalCount = paramInt;
  }

  public int getTotalPage()
  {
    return this.totalPage;
  }

  public void setTotalPage(int paramInt)
  {
    this.totalPage = paramInt;
  }

  public int getLimit()
  {
    return this.limit;
  }

  public void setLimit(int paramInt)
  {
    this.limit = paramInt;
  }

  public int getOffset()
  {
    return this.offset;
  }

  public void setOffset(int paramInt)
  {
    this.offset = paramInt;
  }

  public int getCurrPage()
  {
    return this.currPage;
  }

  public void setCurrPage(int paramInt)
  {
    this.currPage = paramInt;
  }

  public static Page doPage(Page paramPage)
    throws RException
  {
    if (paramPage == null)
      throw new RException("-1", "分页对象为空");
    if (paramPage.getTotalCount() < 0)
      throw new RException("-1", "记录条数  < 0");
    paramPage.setCurrPage(paramPage.getCurrPage() <= 0 ? 1 : paramPage.getCurrPage());
    paramPage.setLimit(paramPage.getLimit() <= 0 ? 10 : paramPage.getLimit());
    paramPage.setOffset((paramPage.getCurrPage() - 1) * paramPage.getLimit());
    paramPage.setTotalPage(paramPage.getTotalCount() % paramPage.getLimit() > 0 ? Math.abs(paramPage.getTotalCount() / paramPage.getLimit()) + 1 : paramPage.getTotalCount() / paramPage.getLimit());
    return paramPage;
  }

  public static String drawHtml(Page paramPage)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    return localStringBuffer.toString();
  }
}