package com.neighborcell.testament.ui;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public abstract class TsAdapter<ROW> extends ArrayAdapter<ROW>
{
  private LayoutInflater inflater;
  private int rowlayout;

  public TsAdapter(Context context, int rowlayout,  List<ROW> rows)
  {
    super(context, 0, rows);
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.rowlayout = rowlayout;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    if (convertView == null)
    {
      convertView = inflater.inflate(rowlayout, parent, false);
      //holder = new NlHolder();
      //holder.convert(convertView);
      //convertView.setTag(holder);
    }
    else
    {
      //holder = (NlHolder) convertView.getTag();
    }

    ROW row = getItem(position);
    convert(convertView,row);

    return convertView;
  }

  protected abstract void convert(View view, ROW row);
}

