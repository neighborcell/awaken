package com.neighborcell.awaken.db;

import android.database.*;
import android.database.sqlite.*;
import com.neighborcell.testament.db.*;
import java.util.*;
import android.view.View.*;
import com.neighborcell.testament.log.*;

public class EvtEntUpInsMeaning extends TsEnt
{
  private static final String sqlinsert = 
  "insert into tbl_word"
  + " ( id_type, val_word, val_meaning ) "
  + " values"
  + " ( ?, ?, ? )";
  
  private static final String sqldelete = 
  "delete from tbl_word"
  + " where id_word = ? ";
  
  public final DtoMeaning meaning;
  
  public EvtEntUpInsMeaning(DtoMeaning meaning)
  {
    this.meaning = meaning;
  }

  @Override
  public void run(SQLiteDatabase db)
  {
    db.beginTransaction();
    TsLog.info(meaning.toString());
    if(0 < meaning.id_word)
    {
      TsLog.debug();
      db.execSQL(sqldelete,
        new String[]{
          Integer.toString(meaning.id_word)
        });
    }
    if(0 < meaning.val_meaning.trim().length())
    {
      TsLog.debug();
      db.execSQL(sqlinsert,
        new String[]{
          Integer.toString(meaning.id_type),
          meaning.val_word,
          meaning.val_meaning
        });
     }
     db.setTransactionSuccessful();
     db.endTransaction();
  }

  @Override
  public String toString()
  {
    return getClass().getName();
  }
}
