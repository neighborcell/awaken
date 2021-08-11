package com.neighborcell.awaken.ui;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.neighborcell.awaken.*;
import com.neighborcell.awaken.db.*;
import com.neighborcell.testament.event.*;
import com.neighborcell.testament.log.*;
import com.neighborcell.testament.ui.*;
import java.util.*;

public class SceSearch extends ListFragment implements TsBackListener
{
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    //TsLog.debug();
    View root = inflater.inflate(R.layout.sce_search, container, false);

    // open word dialog when empty search
    root.findViewById(R.id.sce_search__empty_add).setOnClickListener(OnClickEditWord);

    // search
    root.findViewById(R.id.sce_search__search).setOnClickListener(ClkSearch);
    ((EditText)root.findViewById(R.id.sce_search__keyword)).setOnEditorActionListener(OnEditKeyword);
    
    // for push back
    TsEvt.req(new EvtSearch(""));
    
    return root;
  }

  // close dialog
  public void onEvt(EvtDig evt)
  {
    if(evt.dig instanceof DigMeaning && !evt.isVisible)
    {
      String keyword = ((TextView)getView().findViewById(R.id.sce_search__title)).getText().toString();
      //TsEvt.run(new TsEvtLog(keyword));
      TsEvt.req(new EvtEditWord(keyword));
    }
  }

  // open word dialog when empty search
  private OnClickListener OnClickEditWord = new OnClickListener(){
    @Override
    public void onClick(View p1)
    {
      String keyword = ((TextView)getView().findViewById(R.id.sce_search__title)).getText().toString();
      //TsEvt.run(new TsEvtLog(keyword));
      TsEvt.req(new EvtEditWord(keyword));
    }
  };

  // words list
  public void onEvt(EvtEditWord evt)
  {
    //TsLog.debug();
    ((TextView)getView().findViewById(R.id.sce_search__title)).setText(evt.getKeyword());
    
    final EvtEntSelectMeaning entMeaning = new EvtEntSelectMeaning(evt.getKeyword());
    TsEvt.req(entMeaning, new TsEvtRes(){
        public void res()
        {
          //TsLog.debug();
        }
        public void fin()
        {
          //List<DtoWord> means = new ArrayList<DtoWord>();
          List<DtoMeaning> means = entMeaning.getMeans();
          means.add(new DtoMeaning(entMeaning.keyword));

          setListAdapter(new AdpMeaning(getContext(), entMeaning.keyword, means));

          getView().findViewById(R.id.sce_search__init).setVisibility(View.GONE);
          getView().findViewById(R.id.sce_search__empty).setVisibility(View.GONE);
          getListView().setVisibility(View.VISIBLE);
        }
    });
  }

  // search
  private TextView.OnEditorActionListener OnEditKeyword = new TextView.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
      if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
      {
        if (event.getAction() == KeyEvent.ACTION_UP)
        {
          search();
        }
        return true;
      }
      return false;
    }
  };

  // search
  private OnClickListener ClkSearch = new OnClickListener(){
    public void onClick(View view)
    {
      search();
    }
  };

  // search
  private void search()
  {
    TsEvt.req(new EvtKeyboard());
    EditText edit = getView().findViewById(R.id.sce_search__keyword);
    String keyword = edit.getText().toString().trim();
    //TsEvt.run(new TsEvtLog(keyword));
    
    TsEvt.req(new EvtSearch(keyword));
  }

  // search
  public void onEvt(EvtSearch evt)
  {
    String keyword = evt.getKeyword();
    if (null == keyword || keyword.isEmpty())
    {
      ((TextView)getView().findViewById(R.id.sce_search__title)).setText(R.string.search_title);
      getView().findViewById(R.id.sce_search__init).setVisibility(View.VISIBLE);
      getView().findViewById(R.id.sce_search__empty).setVisibility(View.GONE);
      getListView().setVisibility(View.GONE);
      setListAdapter(null);
    }
    else
    {
      final EvtEntSelectDistinctWord entWord = new EvtEntSelectDistinctWord(keyword);
      TsEvt.req(entWord, new TsEvtRes(){
          public void res()
          {
            //TsLog.debug();
          }
          public void fin()
          {
            //TsLog.debug();
            //TsEvt.run(new TsEvtLog(entWord.getKeyword()));
            ((TextView)getView().findViewById(R.id.sce_search__title)).setText(entWord.keyword);

            if (entWord.words.isEmpty())
            {
              getView().findViewById(R.id.sce_search__init).setVisibility(View.GONE);
              getView().findViewById(R.id.sce_search__empty).setVisibility(View.VISIBLE);
              getListView().setVisibility(View.GONE);
              setListAdapter(null);
            }
            else
            {
              getView().findViewById(R.id.sce_search__init).setVisibility(View.GONE);
              getView().findViewById(R.id.sce_search__empty).setVisibility(View.GONE);
              getListView().setVisibility(View.VISIBLE);

              //for( String v :entWord.getWords() )
              //{
              //  TsLog.info(v);
              //}
              List<String> words = entWord.words;
              if (! entWord.isMatch)
              {
                words.add(null);
              }
              setListAdapter(new AdpWord(getContext(), entWord.keyword, words));
            }
          }
        });
    }
  }

  @Override
  public boolean onBackPressed()
  {
    if (null == getListAdapter())
    {
      //TsLog.debug();
      return false;
    }
    if (getListAdapter().getClass() == AdpWord.class)
    {
      //TsLog.debug();
      TsEvt.req(new EvtSearch(""));
      return true;
    }
    if (getListAdapter().getClass() == AdpMeaning.class)
    {
      //TsLog.debug();
      String keyword = ((TextView)getView().findViewById(R.id.sce_search__title)).getText().toString();
      TsEvt.req(new EvtSearch(keyword));
      return true;
    }
    return false;
  }

  @Override
  public void onStart()
  {
    super.onStart();
    TsLog.debug();
    TsEvt.addEvtSvr(this);
  }

  @Override
  public void onStop()
  {
    super.onStop();
    TsLog.debug();
    TsEvt.delEvtSvr(this);
  }


}
