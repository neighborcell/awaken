package com.neighborcell.awaken.ui;

public class EvtEditWord
{
  private String keyword = null;

  public EvtEditWord(String keyword)
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
