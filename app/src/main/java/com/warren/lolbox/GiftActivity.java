package com.warren.lolbox;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.warren.lolbox.widget.GiftView;
import com.warren.lolbox.widget.TitleBar;
import com.warren.lolbox.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 天赋Activity
 */
public class GiftActivity extends BaseActivity{

    private ViewPagerIndicator mVpi;
    private List<String> mLstTitle;
    private ViewPager mVp;
    private AdapterViewPager mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        initCtrl();
    }

    private void initCtrl(){
        setTitleBar((TitleBar) findViewById(R.id.titlebar));
        getTitleBar().setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GiftActivity.this, "敬请期待！", Toast.LENGTH_SHORT).show();
            }
        });

        mVpi = (ViewPagerIndicator) findViewById(R.id.vpindicator);
        mVp = (ViewPager) findViewById(R.id.vp);
        mLstTitle = new ArrayList<>();
        mLstTitle.add("攻击");
        mLstTitle.add("防御");
        mLstTitle.add("通用");
        mVpi.setTabItemTitles(mLstTitle);
        mAdapter = new AdapterViewPager();
        mVp.setAdapter(mAdapter);
        mVpi.setViewPager(mVp, 0);




    }



    @Override
	protected boolean goBack() {
		return false;
	}

    private class AdapterViewPager extends PagerAdapter {

        private GiftView[] arrView = new GiftView[3];

        public AdapterViewPager() {
            for (int i = 0; i < getCount(); i++) {
                arrView[i] = new GiftView(GiftActivity.this);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {

            ((ViewPager) arg0).removeView(arrView[arg1]);
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(arrView[arg1]);
            return arrView[arg1];
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

}
