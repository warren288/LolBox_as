package com.warren.lolbox.model;

import android.graphics.Color;
import android.graphics.Paint;

import com.warren.lolbox.AppContext;
import com.warren.lolbox.R;

import java.util.Date;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * AChartEngine图表引擎基类
 * @author:yangsheng
 * @date:2015/6/15
 */
public class ChartEngineModel {

    /**
     * 构建 XYMultipleSeriesDataset.
     *
     * @param titles 每个序列的图例
     * @param xValues X轴的坐标
     * @param yValues Y轴的坐标
     * @return XYMultipleSeriesDataset
     */
    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
                                                   List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    //向DataSet中添加序列.
    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
                            List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale); //这里注意与TimeSeries区别.
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    /**
     * 构建XYMultipleSeriesRenderer.
     *
     * @param colors 每个序列的颜色
     * @param styles 每个序列点的类型(可设置三角,圆点,菱形,方块等多种)
     * @return XYMultipleSeriesRenderer
     */
    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        //整个图表属性设置
        //-->start
        renderer.setAxisTitleTextSize(16);//设置轴标题文字的大小
        renderer.setChartTitleTextSize(40);//设置整个图表标题文字的大小
        renderer.setLabelsTextSize(15);//设置轴刻度文字的大小
        renderer.setLegendTextSize(15);//设置图例文字大小
        renderer.setPointSize(5f);//设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
        renderer.setMargins(new int[] { 20, 30, 15, 20 });//设置图表的外边框(上/左/下/右)
        //-->end

        //以下代码设置没个序列的颜色.
        //-->start
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);//设置颜色
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
        //-->end
    }

    /**
     * 设置renderer的一些属性.
     *
     * @param renderer 要设置的renderer
     * @param title 图表标题
     * @param xTitle X轴标题
     * @param yTitle Y轴标题
     * @param xMin X轴最小值
     * @param xMax X轴最大值
     * @param yMin Y轴最小值
     * @param yMax Y轴最大值
     * @param axesColor X轴颜色
     * @param labelsColor Y轴颜色
     */
    public void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    /**
     * 构建和时间有关的XYMultipleSeriesDataset,这个方法与buildDataset在参数上区别是需要List<Date[]>作参数.
     *
     * @param titles 序列图例
     * @param xValues X轴值
     * @param yValues Y轴值
     * @return XYMultipleSeriesDataset
     */
    protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
                                                       List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            TimeSeries series = new TimeSeries(titles[i]);//构建时间序列TimeSeries,
            Date[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    /**
     * 构建单个CategorySeries,可用于生成饼图,注意与buildMultipleCategoryDataset(构建圆环图)相区别.
     *
     * @param title the series titles
     * @param values the values
     * @return the category series
     */
    protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            series.add("Project " + ++k, value);
        }

        return series;
    }

    /**
     * 构建MultipleCategorySeries,可用于构建圆环图(每个环是一个序列)
     *
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
                                                                  List<String[]> titles, List<double[]> values) {
        MultipleCategorySeries series = new MultipleCategorySeries(title);
        int k = 0;
        for (double[] value : values) {
            series.add(2007 + k + "", titles.get(k), value);
            k++;
        }
        return series;
    }

    /**
     * 构建DefaultRenderer.
     *
     * @param colors 每个序列的颜色
     * @return DefaultRenderer
     */
    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[] { 20, 30, 15, 0 });
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    /**
     * 构建XYMultipleSeriesDataset,适用于柱状图.
     *
     * @param titles 每中柱子序列的图列
     * @param catagories 每个柱子的描述
     * @param values 柱子的高度值
     * @return XYMultipleSeriesDataset
     */
    public XYMultipleSeriesDataset buildBarDataset(String[] titles, List<String[]> catagories, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            String[] c = catagories.get(i);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(c[k], v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    /**
     * 构建XYMultipleSeriesRenderer,适用于柱状图.
     *
     * @param colors 每个序列的颜色
     * @return XYMultipleSeriesRenderer
     */
    public XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(18);
        renderer.setShowGridY(false);
//        renderer.setShowGridX(true);
//        renderer.setGridColor(Color.GRAY);
        renderer.setBackgroundColor(AppContext.getApp().getResources().getColor(R.color.lightgrey));
        renderer.setApplyBackgroundColor(true);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(18);
        renderer.setLegendTextSize(18);
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setPanEnabled(false, false);
        renderer.setZoomRate(1f);
        renderer.setBarSpacing(0.5f);
        renderer.setMarginsColor(AppContext.getApp().getResources().getColor(R.color.lightgrey));
        renderer.setAxesColor(Color.BLACK);
        renderer.setShowLabels(true);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);

        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
}
