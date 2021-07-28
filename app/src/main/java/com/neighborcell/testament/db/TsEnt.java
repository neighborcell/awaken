package com.neighborcell.testament.db;

import android.database.sqlite.*;

public abstract class TsEnt
{
  abstract public void run(SQLiteDatabase db);
}
