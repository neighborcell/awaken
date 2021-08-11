package com.neighborcell.testament.event;
import android.os.*;
import com.neighborcell.testament.log.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class TsEvt
{
  final static public String METHOD_MAIN = "onEvt"; //onMain
  final static public String METHOD_PARA = "onAsync"; // onPara

  final static private ExecutorService exec = Executors.newCachedThreadPool();
  static private List<Object> evtsvrs = new ArrayList<Object>();
  
  static synchronized public void addEvtSvr(Object evtsvr)
  {
    evtsvrs.add(evtsvr);
  }

  static synchronized public void delEvtSvr(Object evtsvr)
  {
    evtsvrs.remove(evtsvr);
  }

  static private List<TsEvtRunner> creMainRunners(Object param, TsEvtRes evtres, TsEvtCounter evtcnt)
  {
    List<TsEvtRunner> li = new ArrayList<TsEvtRunner>();
    for (Object evtsvr : evtsvrs)
    {
      for (Method method : evtsvr.getClass().getMethods())
      {
        if (METHOD_MAIN.equals(method.getName())
            && 1 == method.getParameterCount()
            && method.getParameterTypes()[0].isAssignableFrom(param.getClass())
            )
        {
          //Log.d("class",hooker.toString());
          //Log.d("method.getParameterTypes()[0]",method.getParameterTypes()[0].toString());
          //Log.d("param.getClass()",param.getClass().toString());
          //Log.d("test1", param.getClass().isAssignableFrom( method.getParameterTypes()[0] ) ? "true":"false" );
          //Log.d("test2", method.getParameterTypes()[0].isAssignableFrom( param.getClass() ) ? "true":"false" );
          
          li.add(new TsEvtRunner(evtsvr, method, param, evtres, evtcnt));
        }
      }
    }
    return li;
  }

  static private List<TsEvtRunner> creParaRunners(Object param, TsEvtRes evtres, TsEvtCounter evtcnt)
  {
    List<TsEvtRunner> li = new ArrayList<TsEvtRunner>();
    for (Object evtsvr : evtsvrs)
    {
      for (Method method : evtsvr.getClass().getMethods())
      {
        if (METHOD_PARA.equals(method.getName())
            && 1 == method.getParameterCount()
            && method.getParameterTypes()[0].isAssignableFrom(param.getClass())
            )
        {
          //Log.d("class",hooker.toString());
          //Log.d("method.getParameterTypes()[0]",method.getParameterTypes()[0].toString());
          //Log.d("param.getClass()",param.getClass().toString());
          //Log.d("test1", param.getClass().isAssignableFrom( method.getParameterTypes()[0] ) ? "true":"false" );
          //Log.d("test2", method.getParameterTypes()[0].isAssignableFrom( param.getClass() ) ? "true":"false" );
          
          li.add(new TsEvtRunner(evtsvr, method, param, evtres, evtcnt));
        }
      }
    }
    return li;
  }
  
  static synchronized public void req(Object param)
  {
    req(param, null);
  }

  static synchronized public void req(Object param, TsEvtRes evtres)
  {
    //TsLog.info(param.toString());
    final Handler handler = new Handler(Looper.getMainLooper());
    TsEvtCounter evtcnt = new TsEvtCounter();
    List<TsEvtRunner> mainRunners = creMainRunners(param,evtres,evtcnt);
    List<TsEvtRunner> paraRunners = creParaRunners(param,evtres,evtcnt);
    evtcnt.setCnt( mainRunners.size() + paraRunners.size() );
    for(TsEvtRunner mainRunner : mainRunners)
    {
      handler.post(mainRunner);
    }
    for(TsEvtRunner paraRunner : paraRunners)
    {
      exec.submit(paraRunner);
    }
  }

  static private void callback(final TsEvtRes evtres,final boolean isFin)
  {
    //TsLog.info(evt.toString());
    if(isFin)
    {
      req(evtres);
    }
    final Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable(){
        public void run()
        {
          //TsLog.info(evt.toString());
          try
          {
            evtres.res();
            if(isFin){
              evtres.fin();
            }
          }
          catch (Exception e)
          {
            TsLog.err(e);
          }
        }
      });
  }
  
  private static class TsEvtCounter{
    private int evtcnt;
    
    public synchronized void setCnt(int cnt)
    {
      this.evtcnt = cnt;
    }
    public synchronized boolean dec()
    {
      this.evtcnt--;
      if(0>=evtcnt)
      {
        return true;
      }
      return false;
    }
  }
  
  private static class TsEvtRunner implements Runnable
  {
    final private Object body;
    final private Method method;
    final private Object param;
    final private TsEvtRes evtres;
    private TsEvtCounter evtcnt;

    TsEvtRunner(Object body, Method method, Object param, TsEvtRes evtres, TsEvtCounter evtcnt)
    {
      this.body = body;
      this.method = method;
      this.param = param;
      this.evtres = evtres;
      this.evtcnt = evtcnt;
    }

    @Override
    public void run()
    {
      try
      {
        method.invoke(body, param);
      }
//    catch (InvocationTargetException e)
//    {
//      // 呼び出したメソッドの中で例外がおきたとき
//      TsLog.err(e);
//    }
//    catch (IllegalAccessException e)
//    {
//      // アクセスできないメソッドにアクセスしたとき
//      TsLog.err(e);
//    }
      catch (Exception e)
      {
        TsLog.err(e);
      }
      finally{
        boolean isFin = evtcnt.dec();
        if (null != evtres)
        {
//          TsLog.info(body.toString());
//          TsLog.info(param.toString());
//          TsLog.info(rtn.toString());
          TsEvt.callback(evtres,isFin);
        }
      }
    }
  }
}

