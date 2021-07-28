package com.neighborcell.awaken;

import android.app.*;
import android.content.*;
import android.database.sqlite.*;
import android.os.*;
import android.view.inputmethod.*;
import com.neighborcell.awaken.ui.*;
import com.neighborcell.testament.db.*;
import com.neighborcell.testament.event.*;
import com.neighborcell.testament.log.*;
import com.neighborcell.testament.ui.*;
import android.view.*;
import android.widget.*;
import android.transition.*;
import java.util.*;

public class ActMain extends Activity 
{
  static final private int DB_VER = 1;
  static final private String DB_FILE = "main.db";
  private TsDb dbMain = null;

  private List<Integer> frags = new ArrayList<Integer>();

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_main);
    TsLog.debug();
    
    try
    {
      dbMain = new TsDb(getApplicationContext(), DB_FILE, DB_VER);
    }
    catch (Exception e)
    {
      TsLog.err(e);
      throw new Error("Unable to create database");
    }

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    Fragment fragment = new SceSearch();
    fragmentTransaction.replace(R.id.act_main__scene, fragment);
    fragmentTransaction.commit();
    //TsLog.info("SceSearch ; " + fragment.hashCode());
    frags.add(fragment.hashCode());
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    TsLog.debug();
    TsEvt.addEvtSvr(this);
  }

  @Override
  protected void onStop()
  {
    TsLog.debug();
    //((ViwLog)findViewById(R.id.act_log)).onStop();
    TsEvt.delEvtSvr(this);
    super.onStop();
  }

  @Override
  public void onBackPressed()
  {
    TsLog.debug();
    boolean isBacked = false;
    try
    {
      Map<Integer,Fragment> fragmap = new HashMap<Integer,Fragment>();
      FragmentManager fragmentManager = getFragmentManager();
      for (Fragment frag : fragmentManager.getFragments())
      {
        fragmap.put(frag.hashCode(), frag);
      }

      Fragment frag = fragmap.get(frags.get(frags.size() - 1));
      TsLog.info(frag.toString());
      if (null == frag)
      {
        TsLog.debug();
        isBacked = false;
      }
      else if (frag instanceof TsBackListener)
      {
        TsLog.debug();
        isBacked |= ((TsBackListener)frag).onBackPressed();
      }
      else
      {
        TsLog.debug();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(frag);
        frags.remove((Integer)frag.hashCode());
        fragmentTransaction.commit();
        isBacked = true;
      }
    }
    catch (Exception e)
    {
      TsLog.err(e);
    }
    if (!isBacked)
    {
      TsLog.debug();
      super.onBackPressed();
    }
  }

  public void onEvt(EvtDig dig)
  {
    TsLog.debug();
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    if (dig.isVisible)
    {
      frags.add(dig.dig.hashCode());
      //TsLog.info("dig ; " + dig.dig.hashCode());
      fragmentTransaction.add(R.id.act_main__dialog, dig.dig);
    }
    else
    {
      fragmentTransaction.remove(dig.dig);
      frags.remove((Integer)dig.dig.hashCode());
    }
    fragmentTransaction.commit();
  }

  public void onEvt(Object obj)
  {
    TsLog.info(obj.toString());
  }

  public void onAsync(TsEnt ent)
  {
    SQLiteDatabase db = dbMain.getDatabase();
    //TsLog.info(db.getPath());
    try
    {
      ent.run(db);
    }
    catch (Exception e)
    {
      //TsLog.err(e);
      TsEvt.req(new TsEvtLog(e));
    }
    finally
    {
      db.close();
    }
  }

  public void onEvt(EvtKeyboard evt)
  {
    // ソフトキーボードを隠す
    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
  }

}
