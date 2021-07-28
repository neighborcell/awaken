package com.neighborcell.awaken.ui;

public class EvtSearch
{
  private String keyword = null;
  
  public EvtSearch(String keyword)
  {
    this.keyword = keyword;
  }
  
  public String getKeyword()
  {
    return this.keyword;
  }

  @Override
  public String toString()
  {
    String keyword = null == this.keyword ? "null" : this.keyword;
    return getClass().getName() + " keyword=" + keyword;
  }
}
