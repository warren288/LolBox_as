package com.warren.lolbox;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.warren.lolbox.model.IListener;
import com.warren.lolbox.model.SortModel;
import com.warren.lolbox.model.bean.AllHeroList;
import com.warren.lolbox.model.bean.HeroSimple;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.util.CharacterParser;
import com.warren.lolbox.widget.ClearEditText;
import com.warren.lolbox.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 英雄背景故事列表
 * @author:yangsheng
 * @date:2015/9/1
 */
public class HeroBackgroundActivity extends BaseActivity {

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> mLstSm;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herobackground);
        initCtrl();
        requestData();
    }

    private void initCtrl() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BaseKitManager.openHeroDetail(HeroBackgroundActivity.this, mLstSm.get(position).getNameEng());
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(getApplication(), ((SortModel) adapter.getItem(position)).getName(),
//                        Toast.LENGTH_SHORT).show();
            }
        });

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 根据a-z进行排序源数据
        adapter = new SortAdapter(this, mLstSm);
        sortListView.setAdapter(adapter);

    }

    private void requestData(){
        httpGetAndParse(URLUtil.getURL_HeroList("all"), SystemConfig.getIntance().getCommHead(),
                AllHeroList.class, new IListener<AllHeroList>() {

                    @Override
                    public void onCall(AllHeroList t) {

                        if (t != null) {
                            List<HeroSimple> lstHs = t.getAll();
                            new AsyncTransfer().execute(lstHs);
                        }

                    }
                });
    }


    private SortModel fillData(HeroSimple hs){
        SortModel sortModel = new SortModel();
        sortModel.setName(hs.getCnName());
        sortModel.setTitle(hs.getTitle());
        sortModel.setNameEng(hs.getEnName());
        //汉字转换成拼音
        String pinyinName = characterParser.getSelling(hs.getCnName());
        String sortStringName = pinyinName.substring(0, 1).toUpperCase();
        String pinyinTitle = characterParser.getSelling(hs.getTitle());
        String sortStringTitle = pinyinTitle.substring(0, 1).toUpperCase();

        // 正则表达式，判断首字母是否是英文字母
        if(sortStringName.matches("[A-Z]")){
            sortModel.setSortLetters(sortStringName.toUpperCase());
        }else{
            sortModel.setSortLetters("#");
        }
        if(sortStringTitle.matches("[A-Z]")){
            sortModel.setSortLettersTitle(sortStringTitle.toUpperCase());
        }else{
            sortModel.setSortLettersTitle("#");
        }
        return sortModel;
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = mLstSm;
        }else{
            filterDateList.clear();
            for(SortModel sortModel : mLstSm){
                if(sortModel.getName().indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(sortModel.getName()).startsWith(filterStr.toString())
                        ||sortModel.getTitle().indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(sortModel.getTitle()).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    protected boolean goBack() {
        return false;
    }

    private static class SortAdapter extends BaseAdapter implements SectionIndexer {
        private List<SortModel> lstSm = null;
        private Context mContext;

        public SortAdapter(Context mContext, List<SortModel> lstSm) {
            this.mContext = mContext;
            this.lstSm = lstSm;
        }

        /**
         * 当ListView数据发生变化时,调用此方法来更新ListView
         * @param list
         */
        public void updateListView(List<SortModel> list){
            this.lstSm = list;
            notifyDataSetChanged();
        }

        public int getCount() {
            return this.lstSm == null ? 0 : this.lstSm.size();
        }

        public Object getItem(int position) {
            return lstSm.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup arg2) {
            ViewHolder viewHolder = null;
            final SortModel mContent = lstSm.get(position);
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.herobackgrounditem, null);
                viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
                viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if(position == getPositionForSection(section)){
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(mContent.getSortLettersTitle());
            }else{
                viewHolder.tvLetter.setVisibility(View.GONE);
            }

            viewHolder.tvTitle.setText(this.lstSm.get(position).getTitle() + " "
                                            + this.lstSm.get(position).getName());

            return view;

        }



        final static class ViewHolder {
            TextView tvLetter;
            TextView tvTitle;
        }


        /**
         * 根据ListView的当前位置获取分类的首字母的Char ascii值
         */
        public int getSectionForPosition(int position) {
            return lstSm.get(position).getSortLettersTitle().charAt(0);
        }

        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        public int getPositionForSection(int section) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = lstSm.get(i).getSortLettersTitle();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }

            return -1;
        }

        /**
         * 提取英文的首字母，非英文字母用#代替。
         *
         * @param str
         * @return
         */
        private String getAlpha(String str) {
            String  sortStr = str.trim().substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortStr.matches("[A-Z]")) {
                return sortStr;
            } else {
                return "#";
            }
        }

        @Override
        public Object[] getSections() {
            return null;
        }
    }


    class AsyncTransfer extends AsyncTask<List<HeroSimple>, Integer, List<SortModel>>{

        @Override
        protected List<SortModel> doInBackground(List<HeroSimple>... params) {
            List<SortModel> lstSmTitle = new ArrayList<SortModel>();
            for(HeroSimple hs : params[0]){
                SortModel sm = fillData(hs);
                lstSmTitle.add(sm);
            }
            Collections.sort(lstSmTitle, pinyinComparator);
            return lstSmTitle;
        }

        @Override
        protected void onPostExecute(List<SortModel> result) {
            super.onPostExecute(result);
            mLstSm = result;
            adapter.updateListView(mLstSm);
        }
    }


    public class PinyinComparator implements Comparator<SortModel> {

        public int compare(SortModel o1, SortModel o2) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                int comp = o1.getSortLettersTitle().compareTo(o2.getSortLettersTitle());
                if (comp == 0){
                    comp = o1.getSortLetters().compareTo(o2.getSortLetters());;
                }
                return comp;
            }
        }

    }

}
