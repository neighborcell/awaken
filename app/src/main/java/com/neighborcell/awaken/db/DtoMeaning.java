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
}
