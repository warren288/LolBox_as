package com.warren.lolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.warren.lolbox.model.IListener;
import com.warren.lolbox.model.bean.HeroWinRateItem;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.widget.TitleBar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 英雄胜率榜
 * @author:yangsheng
 * @date:2015/9/16
 */
public class HeroWinRateActivity extends BaseActivity {

    private ListView mLvWinRate;
    private TitleBar mTb;
    private AdapterWinRate mAdapter;
    private List<HeroWinRateItem> mLstData;

    private ImageLoader mImgLoader;

    private Comparator<HeroWinRateItem> mComparator = new Comparator<HeroWinRateItem>() {
        @Override
        public int compare(HeroWinRateItem heroWinRateItem, HeroWinRateItem heroWinRateItem2) {
            if(heroWinRateItem.getPresentRate() < heroWinRateItem2.getPresentRate()){
                return 1;
            }else if(heroWinRateItem.getPresentRate() > heroWinRateItem2.getPresentRate()){
                return -1;
            }else if(heroWinRateItem.getWinRate() < heroWinRateItem2.getWinRate()){
                return 1;
            }else if(heroWinRateItem.getWinRate() > heroWinRateItem2.getWinRate()){
                return -1;
            }else{
                return 0;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herowinrate);
        mImgLoader = AppContext.getApp().getImgLoader();
        initCtrl();
        requestData();
    }

    private void initCtrl(){
        mLvWinRate = (ListView) findViewById(R.id.lv_herowinrate);
        mTb = (TitleBar) findViewById(R.id.tb);
        mAdapter = new AdapterWinRate();
        mLvWinRate.setAdapter(mAdapter);
    }

    private void requestData(){
        httpGetAndParseList(URLUtil.getUrl_HeroWinRate(0), null, HeroWinRateItem.class, new IListener<List<HeroWinRateItem>>() {
            @Override
            public void onCall(List<HeroWinRateItem> heroWinRateItems) {
                mLstData = heroWinRateItems;
                Collections.sort(mLstData, mComparator);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private class AdapterWinRate extends BaseAdapter{

        @Override
        public int getCount() {
            return mLstData == null ? 0 : mLstData.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(HeroWinRateActivity.this).inflate(
                        R.layout.activity_winrate_item, parent, false);
                holder = new ViewHolder();
                holder.img = (ImageView) convertView.findViewById(R.id.img_hero);
                holder.tvPreRate = (TextView) convertView.findViewById(R.id.tv_prerate);
                holder.tvWinRate = (TextView) convertView.findViewById(R.id.tv_winrate);
                holder.tvPreTime = (TextView) convertView.findViewById(R.id.tv_pretimes);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            HeroWinRateItem hrri = mLstData.get(position);
            mImgLoader.displayImage(URLUtil.getUrl_HeroWinRateImg(hrri.getNameUS()), holder.img);
            holder.tvPreTime.setText("" + hrri.getTotalPresent());
            holder.tvWinRate.setText("" + hrri.getWinRate());
            holder.tvPreRate.setText("" + hrri.getPresentRate());

            return convertView;
        }

        class ViewHolder{
            ImageView img;
            TextView tvPreRate;
            TextView tvWinRate;
            TextView tvPreTime;
        }
    }

    @Override
    protected boolean goBack() {
        return false;
    }


}
