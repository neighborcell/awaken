package com.neighborcell.awaken.ui;
import android.app.*;

public class EvtBack
{
  public final int target;
  
  public EvtBack(int target)
  {
    this.target = target;
  }
  
  boolean isTarget(Fragment frag)
  {
    return target == frag.hashCode();
  }
}
