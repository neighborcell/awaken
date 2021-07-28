package com.neighborcell.awaken.ui;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.neighborcell.awaken.*;
import com.neighborcell.awaken.db.*;
import com.neighborcell.awaken.ui.*;
import com.neighborcell.testament.event.*;
import com.neighborcell.testament.log.*;
import com.neighborcell.testament.ui.*;
import java.util.*;

public class AdpMeaning extends TsAdapter<DtoMeaning>
{
  private final String keyword;
  
  public AdpMeaning(Context cntx, String keyword, List<DtoMeaning> list)
  {
    super(cntx,R.layout.row_meaning,list);
    this.keyword = keyword;
  }

  @Override
  protected void convert(View view, final DtoMeaning row)
  {
    if(0 < row.id_word ){
      view.findViewById(R.id.row_meaning__add).setVisibility(View.GONE);
      view.findViewById(R.id.row_meaning__edit).setVisibility(View.VISIBLE);
      ((TextView)view.findViewById(R.id.row_meaning__type)).setText(row.val_type);
      ((TextView)view.findViewById(R.id.row_meaning__meaning)).setText(row.val_meaning);
    }else{
      view.findViewById(R.id.row_meaning__add).setVisibility(View.VISIBLE);
      view.findViewById(R.id.row_meaning__edit).setVisibility(View.GONE);
    }
    
    view.setOnClickListener( new OnClickListener(){
      public void onClick(View v){
        TsEvt.req(new EvtEditMeaning(row));
      }
    });
  }
}
