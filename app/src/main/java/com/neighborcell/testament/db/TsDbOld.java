package com.neighborcell.testament.db;

import android.content.*;
import android.database.sqlite.*;
import java.io.*;
import com.neighborcell.testament.log.*;

public class TsDbOld extends SQLiteOpenHelper
{
  //private static String DB_NAME = "main";
  //private static String DB_NAME_ASSET = "main.db";
  private static final int DATABASE_VERSION = 1;

  //private final Context mContext;
  private final File mDatabasePath;

  public TsDbOld(Context appCxt, String dbname, String dbfile)
  {
    super(appCxt, dbname, null, DATABASE_VERSION);
    //mContext = context;
    //mDatabasePath = mContext.getDatabasePath(DB_NAME);
    mDatabasePath = new File(appCxt.getExternalFilesDir(null), dbfile);
    TsLog.info( mDatabasePath.getAbsolutePath());
    createDatabase(appCxt,dbfile);
  }

  /**
   * asset に格納したデータベースをコピーするための空のデータベースを作成する
   */
  public void createDatabase(Context appCxt, String dbfile)
  {
    boolean dbExist = checkDatabaseExists();

    if (dbExist)
    {
      // すでにデータベースは作成されている
      TsLog.info("already exist db");
    }
    else
    {
      TsLog.info("copy from base db");
      // このメソッドを呼ぶことで、空のデータベースがアプリのデフォルトシステムパスに作られる
      SQLiteDatabase db = getReadableDatabase();
      db.close();

      try
      {
        // asset に格納したデータベースをコピーする
        copyDatabaseFromAsset(appCxt, dbfile);

        String dbPath = mDatabasePath.getAbsolutePath();
        TsLog.info(dbPath);
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
          checkDb.setVersion(DATABASE_VERSION);
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
  private boolean checkDatabaseExists()
  {
    String dbPath = mDatabasePath.getAbsolutePath();
    TsLog.info(dbPath);
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
    int newVersion = DATABASE_VERSION;
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
  private void copyDatabaseFromAsset(Context appCxt, String dbfile) throws IOException
  {

    // asset 内のデータベースファイルにアクセス
    InputStream mInput = appCxt.getAssets().open(dbfile);

    // デフォルトのデータベースパスに作成した空のDB
    OutputStream mOutput = new FileOutputStream(mDatabasePath);
    TsLog.info(mDatabasePath.getAbsolutePath());
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

  @Override
  public void onCreate(SQLiteDatabase db)
  {
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  {
  }
}

