package com.neighborcell.testament.log;

import android.content.*;
import android.os.*;
import android.util.*;
import java.io.*;
import java.text.*;
import java.util.*;

public final class TsLog
{
  //private static final String DIR = "/neighborcell/";
  private static final String FNAME = "app.log";

  private static String tag = "TsLog";
  //private static String path = DIR + tag + "/";

  private static final String crlf = System.getProperty("line.separator");
  private static boolean isLoggable = false;

  private static Context appCxt = null;
  
  public static void init(Context appCxt, String tag, boolean isLoggable)
  {
    TsLog.appCxt = appCxt;
    TsLog.tag = tag;
    TsLog.isLoggable = isLoggable;
    info("====================================");
  }

  public static void log(TsEvtLog log)
  {
    if (!isLoggable)
    {
      return;
    }
    Log.i(tag, log.getMsg());
    fileout(log.getMsg());
  }
  
  public static void debug()
  {
    if (!isLoggable)
    {
      return;
    }
    Log.d(tag, "");
    fileout(formatMessage());
  }

  public static void info(String msg)
  {
    if (!isLoggable)
    {
      return;
    }
    Log.i(tag, msg);
    fileout(formatMessage(msg));
  }

  public static void err(Throwable t)
  {
    if (!isLoggable)
    {
      return;
    }

    Log.e(tag, "", t);
    fileout(formatMessage(t.getMessage()));

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    try
    {
      t.printStackTrace(pw);
      pw.flush();
      pw.close();
      fileout(sw.getBuffer().toString());
      pw = null;
    }
    catch ( Exception e )
    {
      Log.e(tag, "", e);
    }
    finally
    {
      if (null != pw)
      {
        try
        {
          pw.close();
        }
        catch ( Exception ex )
        {

        }
      }
    }
  }

  private static String formatMessage()
  {
    int rank = 4;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    sdf.format(c.getTime());

    Thread th = Thread.currentThread();
    StackTraceElement st[] = th.getStackTrace();
    StringBuffer sb = new StringBuffer();

    if (st.length < rank - 1)
    {
      return "";
    }

    sb.append(sdf.format(c.getTime()));
    sb.append("#");
    sb.append(st[rank].getFileName());
    sb.append("[");
    sb.append(st[rank].getLineNumber());
    sb.append("] @");
    sb.append(st[rank].getMethodName());
    sb.append(crlf);

    return sb.toString();
  }

  private static String formatMessage(String msg)
  {
    int rank = 4;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    sdf.format(c.getTime());

    Thread th = Thread.currentThread();
    StackTraceElement st[] = th.getStackTrace();
    StringBuffer sb = new StringBuffer();

    if (st.length < rank - 1)
    {
      return "";
    }

    sb.append(sdf.format(c.getTime()));
    sb.append("#");
    sb.append(st[rank].getFileName());
    sb.append("[");
    sb.append(st[rank].getLineNumber());
    sb.append("]:");
    sb.append(msg);
    sb.append(crlf);

    return sb.toString();
  }

  private static void fileout(String msg)
  {
    fileout(FNAME, msg);
  }

  public static void fileout(String filename, String msg)
  {
    //File file = new File(appCxt.getFilesDir(), filename);
    File file = new File(appCxt.getExternalFilesDir(null), filename);
    //String filePath = Environment.getExternalStorageDirectory() + path + filename;
    //File file = new File(filePath);
    if (!file.getParentFile().exists())
    {
      file.getParentFile().mkdirs();
    }

    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(file, true);
      OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
      BufferedWriter bw = new BufferedWriter(osw);
      bw.write(msg);
      bw.flush();
      bw.close();
    }
    catch (Exception e)
    {
      Log.e(tag, "", e);
    }
    finally
    {
      if (null != fos)
      {
        try
        {
          fos.close();
        }
        catch ( Exception ex )
        {

        }
      }
    }
  }
}

