package com.neighborcell.awaken.db;

import android.database.*;
import android.database.sqlite.*;
import com.neighborcell.testament.db.*;
import java.util.*;
import org.apache.http.auth.*;

public class EvtEntSelectMeaning extends TsEnt
{
  private static final String sql = 
  "select w.id_word, w.id_type, t.val_type, w.val_meaning"
  + " from tbl_word as w"
  + " left join tbl_type as t"
  + " on w.id_type = t.id_type"
  + " where w.val_word = ?"
  + " order by w.id_word ";
  
  public final String keyword;
  
  private List<DtoMeaning> means = new ArrayList<DtoMeaning>();
  
  public EvtEntSelectMeaning(String keyword)
  {
    this.keyword = keyword;
  }
  
  public List<DtoMeaning> getMeans()
  {
    return this.means;
  }

  @Override
  public void run(SQLiteDatabase db)
  {
    means = new ArrayList<DtoMeaning>();
    Cursor cursor = db.rawQuery(sql,
      new String[]{keyword}
    );
    while(cursor.moveToNext()) {
      DtoMeaning mean = new DtoMeaning(keyword);
      mean.id_word = cursor.getInt(0);
      mean.id_type = cursor.getInt(1);
      mean.val_type = cursor.getString(2);
      mean.val_meaning = cursor.getString(3);
      means.add(mean);
    }
  }
  
  @Override
  public String toString()
  {
    String keyword = null == this.keyword ? "null" : this.keyword;
    return getClass().getName() + " keyword=" + keyword;
  }
}
