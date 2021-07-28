package com.neighborcell.awaken.db;

import android.database.*;
import android.database.sqlite.*;
import com.neighborcell.testament.db.*;
import java.util.*;
import android.view.View.*;

public class EvtEntUpInsMeaning extends TsEnt
{
  private static final String sqlinsert = 
  "insert into tbl_word"
  + " ( id_type, val_word, val_meaning ) "
  + " values"
  + " ( ?, ?, ? )";

  public final DtoMeaning meaning;
  
  public EvtEntUpInsMeaning(DtoMeaning meaning)
  {
    this.meaning = meaning;
  }

  @Override
  public void run(SQLiteDatabase db)
  {
    if(0 == meaning.id_word)
    {
      db.execSQL(sqlinsert,
        new String[]{
          Integer.toString(meaning.id_type),
          meaning.val_word,
          meaning.val_meaning
          });
    }
  }

  @Override
  public String toString()
  {
    return getClass().getName();
  }
}
