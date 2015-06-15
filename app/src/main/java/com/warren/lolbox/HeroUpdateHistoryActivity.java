package com.warren.lolbox;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.warren.lolbox.model.IListener;
import com.warren.lolbox.model.bean.HeroUpdateHistory;
import com.warren.lolbox.url.DuowanUtil;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.util.StringUtils;
import com.warren.lolbox.widget.TitleBar;

import java.util.Date;

/**
 * 英雄更新历史
 * @author:yangsheng
 * @date:2015/6/9
 */
public class HeroUpdateHistoryActivity extends BaseActivity {

    public static final String EXTRA_HERO_ENAME = "EXTRA_HERO_ENAME";
    private String mHeroName;
    private TitleBar mTb;
    private ListView mLvHistory;
    private HeroUpdateHistory mHuh;

    private AdapterHeroHistory mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroupdatehistory);
        mHeroName = getIntent().getStringExtra(EXTRA_HERO_ENAME);
        if(StringUtils.isNullOrZero(mHeroName)){
            finish();
        }
        initCtrl();
        requestData();
    }

    private void initCtrl() {
        mTb = (TitleBar) findViewById(R.id.titlebar);
        mLvHistory = (ListView) findViewById(R.id.lv_history);
        mTb.setText("英雄改动");
        mAdapter = new AdapterHeroHistory(this);
        mLvHistory.setAdapter(mAdapter);
    }

    private void requestData() {
        httpGetAndParse(URLUtil.getUrl_HeroUpdateHistory(mHeroName), null, HeroUpdateHistory.class, new IListener<HeroUpdateHistory>() {
            @Override
            public void onCall(HeroUpdateHistory heroUpdateHistory) {
                mHuh = heroUpdateHistory;
                updateViews();
            }
        });
    }

    private void updateViews() {
        mAdapter.setHeroHistory(mHuh);
        mAdapter.notifyDataSetChanged();
    }

    class AdapterHeroHistory extends BaseAdapter{
        private Context context;
        private HeroUpdateHistory huh;

        public AdapterHeroHistory(Context context) {
            this.context = context;
        }

        public void setHeroHistory(HeroUpdateHistory huh){
            this.huh = huh;
        }

        @Override
        public int getCount() {
            return this.huh == null ? 0 : this.huh.getChangeLog().size();
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
            if(null == convertView){
                convertView = LayoutInflater.from(this.context).inflate(R.layout.heroupdatehistory_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvVersion.setText("游戏版本:v" + huh.getChangeLog().get(position).getVersion());
            holder.tvDetail.setText(huh.getChangeLog().get(position).getInfo());
            holder.tvTime.setText(DuowanUtil.translateTime(huh.getChangeLog().get(position).getTime()));
            return convertView;
        }

        class ViewHolder{
            public final TextView tvVersion;
            public final TextView tvTime;
            public final TextView tvDetail;

            public ViewHolder(View root) {
                this.tvVersion = (TextView) root.findViewById(R.id.tv_version);
                this.tvTime = (TextView) root.findViewById(R.id.tv_time);
                this.tvDetail = (TextView) root.findViewById(R.id.tv_detail);
            }
        }
    }


    @Override
    protected boolean goBack() {
        return false;
    }

}
