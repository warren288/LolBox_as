package com.warren.lolbox;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.warren.lolbox.model.ChartEngineModel;
import com.warren.lolbox.model.IListener;
import com.warren.lolbox.model.bean.HeroWeekData;
import com.warren.lolbox.model.bean.HeroWeekRate;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.util.ObjectUtil;
import com.warren.lolbox.widget.TitleBar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.chart.BarChart.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 英雄每周数据，图表引擎 AChartEngine
 * @author:yangsheng
 * @date:2015/6/9
 */
public class HeroWeekDataActivity extends BaseActivity {

    public static final String EXTRA_HEROID = "EXTRA_HEROID";

    private TitleBar mTb;
    private TextView mTvOccuRank;
    private TextView mTvAverWinRate;
    private LinearLayout mLlChartEverydayOccuRate;
    private LinearLayout mLlChartEachRankOccuRate;
    private LinearLayout mLlChartEachRankWinRate;

    private String mStrHeroId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrHeroId = getIntent().getStringExtra(EXTRA_HEROID);
        if(mStrHeroId == null){
            finish();
        }
        setContentView(R.layout.activity_heroweekdata);
        initCtrl();
    }


    private void initCtrl(){
        mTb = (TitleBar) findViewById(R.id.tb);
        mTvOccuRank = (TextView) findViewById(R.id.tv_occurace_rank);
        mTvAverWinRate = (TextView) findViewById(R.id.tv_winrate);
        mLlChartEverydayOccuRate = (LinearLayout) findViewById(R.id.ll_everydayoccu_rate);
        mLlChartEachRankOccuRate = (LinearLayout) findViewById(R.id.ll_everyrankoccu_rate);
        mLlChartEachRankWinRate = (LinearLayout) findViewById(R.id.ll_eachrankwin_rate);
        httpGetAndParse(URLUtil.getUrl_HeroWeekData(mStrHeroId), null, HeroWeekData.class, new IListener<HeroWeekData>() {
            @Override
            public void onCall(HeroWeekData heroWeekData) {
                if (null == heroWeekData) {
                    Toast.makeText(HeroWeekDataActivity.this, "查询数据失败", Toast.LENGTH_LONG).show();
                    return;
                }
                mTvOccuRank.setText("" + heroWeekData.getData().getRank());
                mTvAverWinRate.setText("" + heroWeekData.getData().getAverage_win_ratio() / 100);
                initChart(heroWeekData);
            }
        });
    }



    private void initChart(HeroWeekData heroWeekData){
        GraphicalView gvEveryDay = genChart(heroWeekData.getData().getCharts().get(0));
        GraphicalView gvEveryRank = genChart(heroWeekData.getData().getCharts().get(1));
        GraphicalView gvEveryRankWinRate = genChart(heroWeekData.getData().getCharts().get(2));

        mLlChartEverydayOccuRate.addView(gvEveryDay, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLlChartEachRankOccuRate.addView(gvEveryRank, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLlChartEachRankWinRate.addView(gvEveryRankWinRate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private GraphicalView genChart(HeroWeekRate heroWeekRate){

        ChartEngineModel cem = new ChartEngineModel();
        List<String[]> catagories = new ArrayList<String[]>();
        List<double[]> values = new ArrayList<double[]>();
        String[] titles = new String[]{""};
        String[] arrcata = new String[heroWeekRate.getRatio_data().size()];
        double[] arrRate = new double[heroWeekRate.getRatio_data().size()];
        for(int index = 0; index < heroWeekRate.getRatio_data().size(); index++) {
            Map<String, String> map = heroWeekRate.getRatio_data().get(index);
            arrcata[index] = map.get("name");
            arrRate[index] = (Double.parseDouble(map.get("value"))) / 100;
        }
        catagories.add(arrcata);
        values.add(arrRate);//第一种柱子的数值
        XYMultipleSeriesDataset dataset = cem.buildBarDataset(titles, catagories, values);

        double maxrate = ObjectUtil.getMax(arrRate);
        double dYmax = 1;
        if(maxrate > 1 && maxrate < 2){
            dYmax = 2;
        } else if (maxrate > 10 && maxrate < 100){
            dYmax = 100;
        }

        int[] colors = new int[] {Color.parseColor("#" + heroWeekRate.getColor())};
        XYMultipleSeriesRenderer renderer = cem.buildBarRenderer(colors);
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(7.5);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(dYmax);
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.getSeriesRendererAt(0).setChartValuesTextSize(18);
        renderer.setXLabels(0);
        int count_cata = catagories.get(0).length;
        for(int index = 0; index < count_cata; index++){
            renderer.addXTextLabel(index + 1, catagories.get(0)[index]);
        }

        renderer.setYLabels(5);

        GraphicalView gv = ChartFactory.getBarChartView(HeroWeekDataActivity.this, dataset, renderer, Type.STACKED);
        gv.setBackgroundColor(Color.LTGRAY);
        return gv;
    }

    @Override
    protected boolean goBack() {
        return false;
    }

}
