package com.neighborcell.awaken.ui;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.neighborcell.awaken.*;
import com.neighborcell.awaken.ui.*;
import com.neighborcell.testament.event.*;
import com.neighborcell.testament.log.*;
import com.neighborcell.testament.ui.*;
import java.util.*;

public class AdpWord extends TsAdapter<String>
{
  private final String keyword;
  
  public AdpWord(Context cntx, String keyword, List<String> list)
  {
    super(cntx,R.layout.row_word,list);
    this.keyword = keyword;
  }

  @Override
  protected void convert(View view, final String row)
  {
    TextView tv = view.findViewById(R.id.row_word__word);
    if(null != row ){
      tv.setText(row);
    }else{
      tv.setText(R.string.search_add_word);
    }
    
    view.setOnClickListener( new OnClickListener(){
        public void onClick(View v){
          if( null != row ){
            //TsLog.info(row);
            TsEvt.req(new EvtEditWord(row));
          }else{
            TsEvt.req(new EvtEditWord(keyword));
          }
        }
      });
  }
}
