package com.neighborcell.awaken.ui;
import android.content.*;
import android.util.*;
import android.widget.*;
import com.neighborcell.awaken.*;
import com.neighborcell.awaken.db.*;
import java.util.*;

public class ViwSpinnerType extends Spinner
{
  private List<DtoType> types;
  
  public ViwSpinnerType(Context cxt){
    super(cxt);
    init(cxt);
  }
  
  public ViwSpinnerType(Context cxt, AttributeSet attr){
    super(cxt,attr);
    init(cxt);
  }
  
  private void init(Context cxt) {
    
  }
  
  public void setList(List<DtoType> types, int defaultIdType){
    this.types = types;
    
    int cnt = 0;
    int select = 0;
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    for(DtoType typ : types)
    {
      adapter.add(typ.val_type);
      if(typ.id_type == defaultIdType)
      {
        select = cnt;
      }
      cnt++;
    }
    this.setAdapter(adapter);
    this.setSelection(select);
  }
  
  public DtoType getSelect()
  {
    return types.get(getSelectedItemPosition());
  }
}
