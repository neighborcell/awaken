package com.neighborcell.awaken.db;

import android.database.*;
import android.database.sqlite.*;
import com.neighborcell.testament.db.*;
import java.util.*;

public class EvtEntSelectDistinctWord extends TsEnt
{
  private static final String sql = 
  "select distinct w.val_word"
  + " from tbl_word as w"
  + " where w.val_word like ?"
  + " order by w.val_word ";
  
  public final String keyword;
  
  public boolean isMatch = false;
  
  public List<String> words = null;

  public EvtEntSelectDistinctWord(String keyword)
  {
    this.keyword = keyword;
  }

  @Override
  public void run(SQLiteDatabase db)
  {
    isMatch = false;
    words = new ArrayList<String>();
    
    Cursor cursor = db.rawQuery(sql,
      new String[]{ '%' + keyword + '%'}
    );
    while(cursor.moveToNext()) {
      words.add( cursor.getString(0) );
      if( keyword.equalsIgnoreCase( cursor.getString(0))){
        isMatch = true;
      }
    }
  }
  
  @Override
  public String toString()
  {
    String keyword = null == this.keyword ? "null" : this.keyword;
    return getClass().getName() + " keyword=" + keyword;
  }
}
