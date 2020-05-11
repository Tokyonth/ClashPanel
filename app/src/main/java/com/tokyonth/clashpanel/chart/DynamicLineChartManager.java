package com.tokyonth.clashpanel.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tokyonth.clashpanel.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DynamicLineChartManager {

    private LineChart lineChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private List<ILineDataSet> lineDataSets;
    private SimpleDateFormat df;//设置日期格式  
    private List<String> timeList; //存储x轴的时间

    //一条曲线
    public DynamicLineChartManager(LineChart mLineChart, String name, int color) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        initLineChart();
        initLineDataSet(name, color);
    }

    //多条曲线
    public DynamicLineChartManager(LineChart mLineChart, List<String> names, List<Integer> colors) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        initData();
        initLineChart();
        initLineDataSet(names, colors);
    }

    @SuppressLint("SimpleDateFormat")
    private void initData() {
        lineDataSets = new ArrayList<>();
        df = new SimpleDateFormat("HH:mm:ss");
        timeList = new ArrayList<>();
        xAxis = lineChart.getXAxis();
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
    }

    /**
     * 初始化LineChar
     */
    private void initLineChart() {
        lineChart.setDrawGridBackground(false);
        //显示边界
        lineChart.setDrawBorders(false);
        //设置XY轴动画效果
        lineChart.animateY(500);
        lineChart.animateX(500);
        /*XY轴的设置***/
        xAxis = lineChart.getXAxis();
        //leftAxis = lineChart.getAxisLeft();
        //rightAxis = lineChart.getAxisRight();
        xAxis.setDrawGridLines(false);
        //leftAxis.setDrawGridLines(false);
        //rightAxis.setDrawGridLines(true);
        //设置Y轴网格线为虚线
        //leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //rightAxis.setEnabled(false);
        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(8);
        xAxis.setValueFormatter((value, axis) -> timeList.get((int) value % timeList.size()));
        //保证Y轴从0开始，不然会上移一点
        //leftAxis.setAxisMinimum(0f);
        //rightAxis.setAxisMinimum(0f);
    }

    private void initLineDataSet(String name, int color) {
        lineDataSet = new LineDataSet(null, name);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setHighLightColor(color);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置曲线填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setDrawCircleHole(false);
        //曲线模式
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);//圆滑曲线
        //添加一个空的 LineData
        lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 初始化折线（多条线）
     */
    private void initLineDataSet(List<String> names, List<Integer> colors) {
        for (int i = 0; i < names.size(); i++) {
            lineDataSet = new LineDataSet(null, names.get(i));
            lineDataSet.setColor(colors.get(i));
            lineDataSet.setLineWidth(3f);
            lineDataSet.setCircleRadius(3f);
            lineDataSet.setColor(colors.get(i));
            //lineDataSet.setDrawFilled(true);
            //lineDataSet.setFillDrawable();
            lineDataSet.setCircleColor(colors.get(i));
            lineDataSet.setHighLightColor(colors.get(i));
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            lineDataSet.setValueTextSize(10f);
            lineDataSets.add(lineDataSet);
        }
        //添加一个空的 LineData
        lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    /**
     * 动态添加数据（一条折线图）
     */
    public void addEntry(int number) {
        //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
        if (lineDataSet.getEntryCount() == 0) {
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);
        //避免集合数据过多，及时清空（做这样的处理，并不知道有没有用，但还是这样做了）
        if (timeList.size() > 11) {
            timeList.clear();
        }
        timeList.add(df.format(System.currentTimeMillis()));
        Entry entry = new Entry(lineDataSet.getEntryCount(), number);
        lineData.addEntry(entry, 0);
        //通知数据已经改变
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        //设置在曲线图中显示的最大数量
        lineChart.setVisibleXRangeMaximum(10);
        //移到某个位置
        lineChart.moveViewToX(lineData.getEntryCount() - 5);
    }

    /**
     * 动态添加数据（多条折线图）
     */
    public void addEntry(List<Integer> numbers) {
        if (lineDataSets.get(0).getEntryCount() == 0) {
            lineData = new LineData(lineDataSets);
            lineChart.setData(lineData);
        }
        if (timeList.size() > 11) {
            timeList.clear();
        }
        timeList.add(df.format(System.currentTimeMillis()));
        for (int i = 0; i < numbers.size(); i++) {
            Entry entry = new Entry(lineDataSet.getEntryCount(), numbers.get(i));
            lineData.addEntry(entry, i);
            lineData.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(6);
            lineChart.moveViewToX(lineData.getEntryCount() - 5);
        }
    }

    /**
     * 设置Y轴值
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);
        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        lineChart.invalidate();
    }

    /**
     * 设置高限制线
     */
    public void setHighLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine line = new LimitLine(high, name);
        line.setLineWidth(4f);
        line.setTextSize(10f);
        line.setLineColor(color);
        line.setTextColor(color);
        leftAxis.addLimitLine(line);
        lineChart.invalidate();
    }

    /**
     * 设置低限制线
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine line = new LimitLine(low, name);
        line.setLineWidth(4f);
        line.setTextSize(10f);
        leftAxis.addLimitLine(line);
        lineChart.invalidate();
    }

    /**
     * 设置描述信息
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }

    /**
     * 设置线条填充背景颜色
     */
    public void setChartFillDrawable(Context context) {
        Drawable drawableDown = context.getResources().getDrawable(R.drawable.background_gradient_chart_down);
        Drawable drawableUp = context.getResources().getDrawable(R.drawable.background_gradient_chart_up);
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSetUp = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            LineDataSet lineDataSetDown = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSetUp.setDrawFilled(true);
            lineDataSetUp.setFillDrawable(drawableUp);

            lineDataSetDown.setDrawFilled(true);
            lineDataSetDown.setFillDrawable(drawableDown);
            lineChart.invalidate();
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView(Context context) {
        LineChartMarkView mv = new LineChartMarkView(context, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }

}
