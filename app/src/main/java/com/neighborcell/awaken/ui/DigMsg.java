package com.neighborcell.awaken.ui;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.neighborcell.awaken.*;
import com.neighborcell.testament.log.*;
import android.view.View.*;
import com.neighborcell.testament.event.*;
import com.neighborcell.testament.ui.*;

public class DigMsg extends Fragment
{
  public final String msg;
  
  public DigMsg(String msg)
  {
    this.msg = msg;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    TsLog.debug();
    View root = inflater.inflate(R.layout.dig_msg, container, false);

    // msg
    ((TextView)root.findViewById(R.id.dig_msg__msg)).setText(msg);

    // ok
    root.findViewById(R.id.dig_msg__btn).setOnClickListener(
      new OnClickListener(){
        public void onClick(View v)
        {
          TsEvt.req(new EvtDig(DigMsg.this,false));
        }
      }
    );

    return root;
  }

  @Override
  public void onStart()
  {
    super.onStart();
    TsLog.debug();
  }

  @Override
  public void onStop()
  {
    super.onStop();
    TsLog.debug();
  }
}
