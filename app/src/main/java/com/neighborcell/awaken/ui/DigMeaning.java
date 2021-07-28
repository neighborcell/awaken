package com.neighborcell.awaken.ui;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.neighborcell.awaken.*;
import com.neighborcell.awaken.ui.*;
import com.neighborcell.testament.event.*;
import com.neighborcell.testament.log.*;
import com.neighborcell.awaken.db.*;

public class DigMeaning extends Fragment
{
  public final DtoMeaning meaning;

  public DigMeaning(DtoMeaning meaning)
  {
    this.meaning = meaning;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    TsLog.debug();
    final View root = inflater.inflate(R.layout.dig_meaning, container, false);

    // title
    ((TextView)root.findViewById(R.id.dig_meaning__title)).setText(meaning.val_word);
    
    // meaning
    ((EditText)root.findViewById(R.id.dig_meaning__meaning)).setText(meaning.val_meaning);
    
    // type
    final EvtEntSelectType reqType = new EvtEntSelectType();
    TsEvt.req(reqType,new TsEvtRes(){
      public void res(){}
      public void fin(){
        ViwSpinnerType spinner = root.findViewById(R.id.dig_meaning__type);
        spinner.setList(reqType.getTypes(),meaning.id_type);
        root.findViewById(R.id.dig_meaning__submit).setEnabled(true);
      }
    });
    
    // cancel
    root.findViewById(R.id.dig_meaning__cancel).setOnClickListener(
      new OnClickListener(){
        public void onClick(View v)
        {
          TsEvt.req(new EvtDig(DigMeaning.this,false));
        }
      }
    );
    
    // submit
    root.findViewById(R.id.dig_meaning__submit).setEnabled(false);
    root.findViewById(R.id.dig_meaning__submit).setOnClickListener(
      new OnClickListener(){
        public void onClick(View v)
        {
          DtoMeaning newMeaning = new DtoMeaning(meaning.val_word);
          newMeaning.id_word = meaning.id_word;
          ViwSpinnerType spinner = root.findViewById(R.id.dig_meaning__type);
          DtoType type = spinner.getSelect();
          newMeaning.id_type = type.id_type;
          newMeaning.val_type = type.val_type;
          newMeaning.val_meaning = ((EditText)root.findViewById(R.id.dig_meaning__meaning)).getText().toString().trim();
          TsEvt.req(new EvtEntUpInsMeaning(newMeaning), new TsEvtRes()
          {
              public void res(){}
              public void fin(){
                TsEvt.req(new EvtDig(DigMeaning.this,false));
              }
          });
        }
      }
    );
    return root;
  }
}
