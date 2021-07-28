package com.neighborcell.awaken.ui;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.neighborcell.awaken.*;
import com.neighborcell.testament.event.*;
import com.neighborcell.testament.log.*;

public class FrgLog extends Fragment
{
  final private String BR = System.getProperty("line.separator");
  
  private StringBuilder buf = new StringBuilder();
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.frg_log, container, false);
    
    TextView tv = view.findViewById(R.id.frg_log__log);
    tv.setAlpha(0.2f);
    tv.setTextSize(10.0f);
    
    return view;
  }

  @Override
  public void onStart()
  {
    super.onStart();
    TsEvt.addEvtSvr(this);
  }

  @Override
  public void onStop()
  {
    super.onStop();
    TsEvt.delEvtSvr(this);
  }
  
//  public void onEvt(TsEvtLog log)
//  {
//    buf.append(log.getMsg());
//    TextView tv = getView().findViewById(R.id.frg_log__log);
//    tv.setText(buf.toString());
//  }
  
  public void onEvt(Object obj)
  {
    //TsLog.info(obj.getClass().getName());
    buf.append(obj.toString());
    buf.append(BR);
    TextView tv = getView().findViewById(R.id.frg_log__log);
    tv.setText(buf.toString());
  }
  
}
