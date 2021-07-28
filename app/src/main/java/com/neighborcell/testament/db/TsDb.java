package com.neighborcell.testament.db;

import android.content.*;
import android.database.sqlite.*;
import com.neighborcell.testament.log.*;
import java.io.*;

public class TsDb
{
  final private File dbfile;
  
  public TsDb(Context appCxt, String dbname, int ver)
  {
    //mContext = context;
    //mDatabasePath = mContext.getDatabasePath(DB_NAME);
    dbfile = new File(appCxt.getExternalFilesDir(null), dbname);
    TsLog.info( dbfile.getAbsolutePath());
    createDatabase(appCxt,dbfile,ver);
  }

  /**
   * asset に格納したデータベースをコピーするための空のデータベースを作成する
   */
  public void createDatabase(Context appCxt, File dbfile,int ver)
  {
    boolean dbExist = checkDatabaseExists(dbfile,ver);

    if (dbExist)
    {
      // すでにデータベースは作成されている
      TsLog.info("already exist db");
    }
    else
    {
      TsLog.info("copy from base db");
      // このメソッドを呼ぶことで、空のデータベースがアプリのデフォルトシステムパスに作られる
      //SQLiteDatabase db = getReadableDatabase();
      SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
      db.close();

      try
      {
        // asset に格納したデータベースをコピーする
        copyDatabaseFromAsset(appCxt, dbfile);

        String dbPath = dbfile.getAbsolutePath();
        //TsLog.info(dbPath);
        SQLiteDatabase checkDb = null;
        try
        {
          checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLiteException e)
        {
          TsLog.err(e);
        }

        if (checkDb != null)
        {
          checkDb.setVersion(ver);
          checkDb.close();
        }

      }
      catch (IOException e)
      {
        TsLog.err(e);
        throw new Error("Error copying database");
      }
    }
  }

  /**
   * 再コピーを防止するために、すでにデータベースがあるかどうか判定する
   *
   * @return 存在している場合 {@code true}
   */
  private boolean checkDatabaseExists(File dbfile, int ver)
  {
    String dbPath = dbfile.getAbsolutePath();
    //TsLog.info(dbPath);
    SQLiteDatabase checkDb = null;
    try
    {
      checkDb = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    catch (SQLiteException e)
    {
      // データベースはまだ存在していない
    }

    if (checkDb == null)
    {
      // データベースはまだ存在していない
      return false;
    }

    int oldVersion = checkDb.getVersion();
    int newVersion = ver;
    checkDb.close();

    if (oldVersion == newVersion)
    {
      // データベースは存在していて最新
      return true;
    }

    // データベースが存在していて最新ではないので削除
    File f = new File(dbPath);
    f.delete();
    return false;
  }

  /**
   * asset に格納したデーだベースをデフォルトのデータベースパスに作成したからのデータベースにコピーする
   */
  private void copyDatabaseFromAsset(Context appCxt, File dbfile) throws IOException
  {

    // asset 内のデータベースファイルにアクセス
    TsLog.info(dbfile.getName());
    InputStream mInput = appCxt.getAssets().open(dbfile.getName());
    
    // デフォルトのデータベースパスに作成した空のDB
    //TsLog.info(dbfile.getAbsolutePath());
    OutputStream mOutput = new FileOutputStream(dbfile);
  
    // コピー
    byte[] buffer = new byte[1024];
    int size;
    while ((size = mInput.read(buffer)) > 0)
    {
      mOutput.write(buffer, 0, size);
    }

    // Close the streams
    mOutput.flush();
    mOutput.close();
    mInput.close();
  }
  
  public SQLiteDatabase getDatabase()
  {
    return SQLiteDatabase.openOrCreateDatabase(dbfile,null);
  }
}
