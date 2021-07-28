package com.neighborcell.awaken;
import android.app.*;
import com.neighborcell.testament.log.*;

public class AwApp extends Application
{

  @Override
  public void onCreate()
  {
    super.onCreate();
    
    TsLog.init(getApplicationContext(),"awaken",true);
    TsLog.debug();
  }
}
