package com.neighborcell.awaken.db;

public class DtoMeaning
{
  public int id_word = 0;
  
  final public String val_word;
  
  public int id_type = 0;
  
  public String val_type = null;
  
  public String val_meaning = null;
  
  public DtoMeaning(String val_word)
  {
    this.val_word = val_word;
  }

  @Override
  public String toString()
  {
    return super.toString()
      + " id_word:" + id_word
      + " val_word:" + val_word
      + " id_type:" + id_type
      + " val_type:" + val_type
      + " val_meaning:" + val_meaning;
  }
}
