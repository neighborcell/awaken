package com.neighborcell.testament.log;

import android.os.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class TsEvtLog
{
  final private String BR = System.getProperty("line.separator");
  
  final private String src;
  final private String msg;

  public TsEvtLog(String msg)
  {
    this.src = msg;
    this.msg = format( msg );
  }

  public String getMsg()
  {
    return msg;
  }

  private String format(String msg)
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
    sb.append(" ");
    sb.append(msg);
    sb.append(BR);

    return sb.toString();
  }
  
  public TsEvtLog(Throwable t)
  {
    String buf = "";
    //fileout(format(t.getMessage()));
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    try
    {
      t.printStackTrace(pw);
      pw.flush();
      pw.close();
      buf = sw.getBuffer().toString();
      pw = null;
    }
    catch ( Exception e )
    {
      //Log.e(tag, "", e);
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
    this.src = this.msg = buf;
  }
  

  @Override
  public String toString()
  {
    String src = null == this.src ? "null" : this.src;
    return getClass().getName() + " " + src;
  }
}
