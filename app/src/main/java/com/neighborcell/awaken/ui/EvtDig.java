package com.neighborcell.awaken.ui;

import android.view.*;
import android.app.*;

public class EvtDig
{
  public final Fragment dig;
  
  public final boolean isVisible;
  
  public EvtDig(Fragment dig, boolean isVisible)
  {
    this.dig = dig;
    this.isVisible = isVisible;
  }
  
  public EvtDig(Fragment dig)
  {
    this.dig = dig;
    this.isVisible = true;
  }
}
