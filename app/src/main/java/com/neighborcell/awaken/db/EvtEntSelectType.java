package com.neighborcell.awaken.db;

import android.database.*;
import android.database.sqlite.*;
import com.neighborcell.awaken.db.*;
import com.neighborcell.testament.db.*;
import java.util.*;

public class EvtEntSelectType extends TsEnt
{
  private static final String sql = 
  "select t.id_type, t.val_type"
  + " from tbl_type as t"
  + " order by t.id_type ";

  private List<DtoType> types = new ArrayList<DtoType>();

  public EvtEntSelectType()
  {
  }

  public List<DtoType> getTypes()
  {
    return this.types;
  }

  @Override
  public void run(SQLiteDatabase db)
  {
    types = new ArrayList<DtoType>();
    Cursor cursor = db.rawQuery(sql,
                                new String[]{}
                                );
    while(cursor.moveToNext()) {
      DtoType type = new DtoType();
      type.id_type = cursor.getInt(0);
      type.val_type = cursor.getString(1);
      types.add(type);
    }
  }

  @Override
  public String toString()
  {
    return getClass().getName();
  }
}
