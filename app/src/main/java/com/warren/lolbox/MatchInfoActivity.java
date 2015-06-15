package com.warren.lolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.warren.lolbox.model.IListener;
import com.warren.lolbox.url.DuowanConfig;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.util.DeviceUtil;
import com.warren.lolbox.util.StringUtils;
import com.warren.lolbox.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 摇一摇得到的当前对战信息
 * @author:yangsheng
 * @date:2015/6/6
 */
public class MatchInfoActivity extends BaseActivity {

    public static final String EXTRA_MATCHJSON = "match_json";
    private String mStrJson;

    private TitleBar mTb;
    private ListView mLv;

    private AdapterMatch mAdapter;
    private Map<String, HashMap<String, Object>> mMatchInfo;

    private List<Map<String, String>> mMatchTransferSelf = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> mMatchTransferEnemy = new ArrayList<Map<String, String>>();

    private int mAverageSelf = 0;
    private int mAverageEnemy = 0;
    private String mStrServer;
    private String mStrSummoner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrJson = getIntent().getStringExtra(EXTRA_MATCHJSON);
        if(StringUtils.isNullOrZero(mStrJson)){
            Toast.makeText(this, "当前没有对战信息", Toast.LENGTH_SHORT);
            finish();
        }
        setContentView(R.layout.activity_matchinfo);
        initCtrl();
        jsonParseMap(mStrJson, new IListener<Map<String, HashMap<String, Object>>>(){
            @Override
            public void onCall(Map<String, HashMap<String, Object>> stringHashMapMap) {
                if(stringHashMapMap == null){
                    Toast.makeText(MatchInfoActivity.this, "当前没有对战信息", Toast.LENGTH_SHORT);
                    finish();
                } else{
                    mMatchInfo = stringHashMapMap;
                    updateData();
                }
            }
        });
    }

    private void initCtrl(){
        mTb = (TitleBar) findViewById(R.id.tb);
        mLv = (ListView) findViewById(R.id.lv_matchinfo);
        mAdapter = new AdapterMatch();
        mTb.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateData(){

        HashMap<String, Object> mapSummonerNames = mMatchInfo.get("gameInfo");
        HashMap<String, Object> mapSummonerInfos = mMatchInfo.get("playerInfo");
        mStrServer = (String)mapSummonerNames.get("sn");
        mStrSummoner = (String)mapSummonerNames.get("pn");
        List<String> lstSelf = (List<String>)mapSummonerNames.get("200_sort");
        List<String> lstEnemy = (List<String>)mapSummonerNames.get("100_sort");
        boolean isChanged = true;
        for(String strSelf : lstSelf){
            if(strSelf.equals(mStrSummoner)){
                isChanged = false;
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", strSelf);
            map.put("heroname", "" + ((Map<String, Object>) mapSummonerNames.get("200")).get(strSelf));
            int nZdl = (Integer)((Map<String, Object>)mapSummonerInfos.get(strSelf)).get("zdl");
            map.put("zdl", "" + nZdl);
            map.put("winrate", "" + ((Map<String, Object>) mapSummonerInfos.get(strSelf)).get("winRate") + "%");
            map.put("total", "" + ((Map<String, Object>) mapSummonerInfos.get(strSelf)).get("total"));
            mMatchTransferSelf.add(map);
            mAverageSelf += nZdl;
        }
        for(String strSelf : lstEnemy) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", strSelf);
            map.put("heroname", "" + ((Map<String, Object>) mapSummonerNames.get("100")).get(strSelf));
            int nZdl = (Integer)((Map<String, Object>)mapSummonerInfos.get(strSelf)).get("zdl");
            map.put("zdl", "" + nZdl);
            map.put("winrate", "" + ((Map<String, Object>) mapSummonerInfos.get(strSelf)).get("winRate") + "%");
            map.put("total", "" + ((Map<String, Object>) mapSummonerInfos.get(strSelf)).get("total"));
            mMatchTransferEnemy.add(map);
            mAverageEnemy += nZdl;
        }
        mAverageSelf /= 5;
        mAverageEnemy /= 5;

        if(isChanged){
            List<Map<String, String>> matchTransferTemp = mMatchTransferSelf;
            mMatchTransferSelf = mMatchTransferEnemy;
            mMatchTransferEnemy = matchTransferTemp;
            int averageTemp = mAverageSelf;
            mAverageSelf = mAverageEnemy;
            mAverageEnemy = averageTemp;
        }

        mLv.setAdapter(mAdapter);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 2 && position <= 6){
                    BaseKitManager.openSummonerDetail(MatchInfoActivity.this, URLUtil.getURL_SummonerDetail(mMatchTransferSelf.get(position - 2).get("name"), mStrServer));
                } else if(position >= 8 && position <= 12){
                    BaseKitManager.openSummonerDetail(MatchInfoActivity.this, URLUtil.getURL_SummonerDetail(mMatchTransferEnemy.get(position - 8).get("name"), mStrServer));
                }
            }
        });

//        mAdapter.notifyDataSetChanged();
    }

    class AdapterMatch extends BaseAdapter{

        @Override
        public int getCount() {
            return 13;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            switch (position){
                case 0:
                    return 0;
                case 1:
                case 7:
                    return 1;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    return 2;
                default:
                    return 0;
            }
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            switch (type){
                case 0:
                    return getViewHead(position, convertView, parent);
                case 1:
                    return getViewTitle(position, convertView, parent);
                case 2:
                    return getViewSummoner(position, convertView, parent);
                default:
                    break;
            }
            return null;
        }

        private View getViewHead(int position, View convertView, ViewGroup parent){
            View vroot = LayoutInflater.from(parent.getContext()).inflate(R.layout.matchinfo_item_head, parent, false);
            ((TextView)vroot.findViewById(R.id.tv_self_zdl)).setText("" + mAverageSelf);
            ((TextView)vroot.findViewById(R.id.tv_enemy_zdl)).setText("" + mAverageEnemy);
            return vroot;
        }
        private View getViewTitle(int position, View convertView, ViewGroup parent){
            TextView tv = new TextView(parent.getContext());
            tv.setPadding(DeviceUtil.dp2Px(parent.getContext(), 5),
                    DeviceUtil.dp2Px(parent.getContext(), 5), DeviceUtil.dp2Px(parent.getContext(), 5),
                    DeviceUtil.dp2Px(parent.getContext(), 5));
            String str = "";
            if(position == 1){
                str = "己方队伍";
            }else{
                str = "敌方队伍";
            }
            tv.setText(str);
            return tv;
        }
        private View getViewSummoner(int position, View convertView, ViewGroup parent){
            View vroot = LayoutInflater.from(parent.getContext()).inflate(R.layout.matchinfo_item_player, parent, false);
            ImageView img = (ImageView) vroot.findViewById(R.id.img);
            TextView tvName = (TextView) vroot.findViewById(R.id.tv_name);
            TextView tvTotal = (TextView) vroot.findViewById(R.id.tv_total);
            TextView tvWinRate = (TextView) vroot.findViewById(R.id.tv_winrate);
            TextView tvZdl = (TextView) vroot.findViewById(R.id.tv_zdl);
            Map<String, String> map = null;
            if(position >= 2 && position <= 6){
                map = mMatchTransferSelf.get(position - 2);
            } else if(position >= 8 && position <= 12){
                map = mMatchTransferEnemy.get(position - 8);
            }
            AppContext.getApp().getImgLoader().displayImage(URLUtil.getURL_HeroImg((String)map.get("heroname"), DuowanConfig.EnumDPI.DPI120x120), img);
            tvName.setText(map.get("name"));
            tvTotal.setText("总场次" + map.get("total"));
            tvWinRate.setText("胜率 " + map.get("winrate"));
            tvZdl.setText(map.get("zdl"));
            return vroot;
        }
    }



    @Override
    protected boolean goBack() {
        return false;
    }
}
