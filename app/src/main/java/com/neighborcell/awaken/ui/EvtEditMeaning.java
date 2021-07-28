package com.neighborcell.awaken.ui;
import com.neighborcell.awaken.db.*;

public class EvtEditMeaning
{
  public final DtoMeaning word;

  public EvtEditMeaning(DtoMeaning word)
  {
    this.word = word;
  }

  @Override
  public String toString()
  {
    String meaning = null == this.word.val_meaning ? "null" : this.word.val_meaning;
    return getClass().getName() + " meaning=" + meaning;
  }
}
