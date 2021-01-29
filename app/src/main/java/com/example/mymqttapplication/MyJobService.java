package com.example.mymqttapplication;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.mymqttapplication.helper.MqttHelper;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.mymqttapplication.MainActivity.chart1;
import static com.example.mymqttapplication.MainActivity.chart2;
import static com.example.mymqttapplication.MainActivity.chart3;
import static com.example.mymqttapplication.MainActivity.chart4;

public class MyJobService extends JobIntentService {

    WaterManagementModel waterManagementModel;
    DatabaseHelper databaseHelper=new DatabaseHelper(this);
    MqttHelper mqttHelper;

    LineDataSet dataSet=new LineDataSet(null,null);
    ArrayList<ILineDataSet> dataSet1=new ArrayList<>();
    LineData data;

    BarDataSet dataSetB;
    ArrayList<IBarDataSet> dataSet1B=new ArrayList<>();
    BarData dataB;

    Date date;
    SimpleDateFormat sdf;
    String newDate;

    Date dateB;
    SimpleDateFormat sdfB;
    String newDateB;

    public static final int JOB_ID = 10;



    public MyJobService() {
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobService.class, JOB_ID, work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        addDataToGraph();
        chart1.invalidate();
        addDataToGraphB();
        chart2.invalidate();
        addDataToGraphFlowA();
        chart3.invalidate();
        addDataToGraphFlowB();
        chart4.invalidate();

        //chart1.invalidate();

        MainActivity.valveStateA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MainActivity.valveStateA.isChecked()){
                    mqttHelper.publishMessageOpenA();
                    Toast.makeText(getApplicationContext(),"Valve A OPEN",Toast.LENGTH_SHORT).show();
                }else{
                    mqttHelper.publishMessageCloseA();
                    Toast.makeText(getApplicationContext(),"Valve A CLOSE",Toast.LENGTH_SHORT).show();
                }

            }
        });

        MainActivity.valveStateB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MainActivity.valveStateB.isChecked()){
                    mqttHelper.publishMessageOpenB();
                    Toast.makeText(getApplicationContext(),"Valve B OPEN",Toast.LENGTH_SHORT).show();
                }else{
                    mqttHelper.publishMessageCloseB();
                    Toast.makeText(getApplicationContext(),"Valve B CLOSE",Toast.LENGTH_SHORT).show();
                }

            }
        });

        MainActivity.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteOne(waterManagementModel);
                Toast.makeText(getApplicationContext(),"Data deleted",Toast.LENGTH_SHORT).show();

                //Toast.makeText(MainActivity.this,"Data deleted",Toast.LENGTH_SHORT).show();
            }
        });

        MainActivity.disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mqttHelper.disconnect();
                    Toast.makeText(getApplicationContext(), "Successfully Disconnected", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Already Disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                //Log.w("Debug", mqttMessage.toString());
                if (topic.contains(MainActivity.topicFlowA)) {
                    Log.w("Flow", mqttMessage.toString());
                    MainActivity.flowRateA.setText(mqttMessage.toString());

                    date = new Date();
                    sdf = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
                    newDate = sdf.format(date);

                    try {
                        waterManagementModel = new WaterManagementModel(-1, newDate, mqttMessage.toString());
                        Toast.makeText(getApplicationContext(), waterManagementModel.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Flow A not inserted", Toast.LENGTH_LONG).show();
                        waterManagementModel = new WaterManagementModel(-1, "NA", "error");
                    }


                    //databaseHelper = new DatabaseHelper(this);
                    boolean successC = databaseHelper.addThree(waterManagementModel);
                    addDataToGraphFlowA();
                    //chart1.invalidate();
                    databaseHelper.close();
                    //db.close();
                    //Toast.makeText(getApplicationContext(), "SuccessFlowA=" + successC, Toast.LENGTH_SHORT).show();
                } else if (topic.contains(MainActivity.topicVolumeA)) {
                    Log.w("Volume", mqttMessage.toString());
                    MainActivity.volumeA.setText(mqttMessage.toString());
                    //mChart.addEntry(Float.parseFloat(mqttMessage.toString()));

                    dateB = new Date();
                    sdfB = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
                    newDateB = sdfB.format(dateB);

                    //val =mqttMessage.toString();
                    try {
                        waterManagementModel = new WaterManagementModel(-1, newDateB, mqttMessage.toString());
                        Toast.makeText(getApplicationContext(), waterManagementModel.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Volume A not inserted", Toast.LENGTH_LONG).show();
                        waterManagementModel = new WaterManagementModel(-1, "NA", "error");
                    }

                    //databaseHelper = new DatabaseHelper(this);
                    boolean successA = databaseHelper.addOne(waterManagementModel);
                    addDataToGraph();
                    chart1.invalidate();
                    databaseHelper.close();
                    //db.close();
                    //Toast.makeText(getApplicationContext(), "SuccessVolumeA=" + successA, Toast.LENGTH_SHORT).show();
                    //Show();



                } else if (topic.contains(MainActivity.topicFlowB)){
                    Log.w("Flow", mqttMessage.toString());
                    MainActivity.flowRateB.setText(mqttMessage.toString());

                    date = new Date();
                    sdf = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
                    newDate = sdf.format(date);

                    //val =mqttMessage.toString();
                    try {
                        waterManagementModel = new WaterManagementModel(-1, newDate, mqttMessage.toString());
                        Toast.makeText(getApplicationContext(), waterManagementModel.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Flow B not inserted", Toast.LENGTH_LONG).show();
                        waterManagementModel = new WaterManagementModel(-1, "NA", "error");
                    }

                    //databaseHelper = new DatabaseHelper(this);
                    boolean successD = databaseHelper.addFour(waterManagementModel);
                    addDataToGraphFlowB();
                    //chart1.invalidate();
                    databaseHelper.close();
                    //db.close();
                    //Toast.makeText(getApplicationContext(), "SuccessFlowB=" + successD, Toast.LENGTH_SHORT).show();
                } else if (topic.contains(MainActivity.topicVolumeB)){
                    Log.w("Volume", mqttMessage.toString());
                    MainActivity.volumeB.setText(mqttMessage.toString());

                    dateB = new Date();
                    sdfB = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
                    newDateB = sdfB.format(dateB);
                    try {
                        waterManagementModel = new WaterManagementModel(-1, newDateB, mqttMessage.toString());
                        Toast.makeText(getApplicationContext(), waterManagementModel.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Volume B not inserted", Toast.LENGTH_LONG).show();
                        waterManagementModel = new WaterManagementModel(-1, "NA", "error");
                    }

                    //databaseHelper = new DatabaseHelper(this);
                    boolean successB = databaseHelper.addTwo(waterManagementModel);
                    addDataToGraphB();
                    //chart1.invalidate();
                    databaseHelper.close();
                    //db.close();
                    //Toast.makeText(getApplicationContext(), "SuccessVolumeB=" + successB, Toast.LENGTH_SHORT).show();
                    //Show();
                } else if (topic.contains(MainActivity.topicLevel)) {
                    Log.w("Level", mqttMessage.toString());
                    MainActivity.tankLevelA.setText(mqttMessage.toString());
                } else if (topic.contains(MainActivity.topicAlertA)) {
                    Log.w("Alert", mqttMessage.toString());
                    MainActivity.mp.start();
                    Toast.makeText(getApplicationContext(), "Alert!!! Tank is FULL!!!", Toast.LENGTH_LONG).show();
                }else if(topic.contains(MainActivity.topicAlertB)){
                    MainActivity.mp.stop();
                    Toast.makeText(getApplicationContext(), "Alert!!! Close Valve!!!", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }
    public void addDataToGraph()
    {
        //databaseHelper = new DatabaseHelper(this);

        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        final ArrayList<String> yData = databaseHelper.queryYData();
        if(yData != null) {
            for (int i = 0; i < yData.size(); i++) {
                if(!TextUtils.isEmpty(yData.get(i))) {
                    BarEntry barEntry =  new BarEntry(i, Float.parseFloat(yData.get(i)));
                    yVals.add(barEntry);
                }
            }
        }

        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = databaseHelper.queryXDataB();

        for (int i = 0; i < xData.size(); i++) {
            xVals.add(xData.get(i));

        }


        // Utils.init(getApplicationContext());

        //data.addDataSet(new Entry(x, y)0);
        chart1.refreshDrawableState();
        dataSetB = new BarDataSet(yVals, "Volume A in Litre");
        dataSet1B = new ArrayList<>();
        dataSet1B.add(dataSetB);
        Utils.init(getApplicationContext());
        dataB = new BarData(dataSet1B);
        chart1.clear();
        dataB.notifyDataChanged();
        chart1.notifyDataSetChanged();
        chart1.setData(dataB);
        chart1.invalidate();
        chart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart1.getDescription().setEnabled(true);
        chart1.getDescription().setText("X axis = TIME , Y axis = Litre");
        //chart1.setTouchEnabled(true);
       // chart1.setDragEnabled(true);
        chart1.setScaleEnabled(true);
        //chart1.setPinchZoom(true);
        chart1.setHorizontalScrollBarEnabled(true);
        //chart.setVisibleXRange(1,10);
        chart1.setVisibleXRangeMaximum(3);
        //chart.moveViewToX(5);
        chart1.moveViewTo(10,10, YAxis.AxisDependency.RIGHT);

        //chart.moveViewTo(0f,data.getDataSets(dataSet1), YAxis.AxisDependency.LEFT)
        //chart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
        //
        //chart.moveViewToX(data.getXMax());


    }

    public void addDataToGraphB() {
        //databaseHelper = new DatabaseHelper(this);

        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        final ArrayList<String> yData = databaseHelper.queryYDataB();
        if (yData != null) {

        for (int j = 0; j < yData.size(); j++) {
            if(!TextUtils.isEmpty(yData.get(j))) {
                BarEntry barEntry = new BarEntry(j, Float.parseFloat(yData.get(j)));
                yVals.add(barEntry);
            }
        }
        }
        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = databaseHelper.queryXDataB();

        for (int i = 0; i < xData.size(); i++) {
            xVals.add(xData.get(i));

        }


        // Utils.init(getApplicationContext());

        //data.addDataSet(new Entry(x, y)0);
        chart2.refreshDrawableState();
        dataSetB = new BarDataSet(yVals, "Volume B in Litre");
        dataSet1B = new ArrayList<>();
        dataSet1B.add(dataSetB);
        Utils.init(getApplicationContext());
        dataB = new BarData(dataSet1B);
        chart2.clear();
        dataB.notifyDataChanged();
        chart2.notifyDataSetChanged();
        chart2.setData(dataB);
        chart2.invalidate();
        chart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart2.getDescription().setEnabled(true);
        chart2.getDescription().setText("X axis = TIME , Y axis = Litre");
        chart2.setTouchEnabled(true);
        chart2.setDragEnabled(true);
        chart2.setScaleEnabled(true);
        chart2.setPinchZoom(true);
        chart2.setHorizontalScrollBarEnabled(true);
        //chart.setVisibleXRange(1,10);
        chart2.setVisibleXRangeMaximum(3);
        //chart.moveViewToX(5);
        chart2.moveViewTo(10,10, YAxis.AxisDependency.RIGHT);

        //chart.moveViewTo(0f,data.getDataSets(dataSet1), YAxis.AxisDependency.LEFT)
        //chart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
        //
        //chart.moveViewToX(data.getXMax());


    }

    public void addDataToGraphFlowA() {
        //databaseHelper = new DatabaseHelper(this);

        final ArrayList<Entry> yVals = new ArrayList<Entry>();
        final ArrayList<String> yData = databaseHelper.queryYDataFlowA();
        if (yData != null) {

            for (int j = 0; j < yData.size(); j++) {
                if(!TextUtils.isEmpty(yData.get(j))) {
                    Entry lineEntry = new Entry(j, Float.parseFloat(yData.get(j)));
                    yVals.add(lineEntry);
                }
            }
        }
        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = databaseHelper.queryXData();

        for (int i = 0; i < databaseHelper.queryXData().size(); i++) {
            xVals.add(xData.get(i));

        }


        // Utils.init(getApplicationContext());

        //data.addDataSet(new Entry(x, y)0);
        chart3.refreshDrawableState();
        dataSet = new LineDataSet(yVals, "Flow Rate A in Litre/min");
        dataSet1 = new ArrayList<>();
        dataSet1.add(dataSet);
        Utils.init(getApplicationContext());
        data = new LineData(dataSet1);
        chart3.clear();
        data.notifyDataChanged();
        chart3.notifyDataSetChanged();
        chart3.setData(data);
        chart3.invalidate();
        chart3.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart3.getDescription().setEnabled(true);
        chart3.getDescription().setText("X axis = TIME , Y axis = Litre/min");
        chart3.setTouchEnabled(true);
        chart3.setDragEnabled(true);
        chart3.setScaleEnabled(true);
        chart3.setPinchZoom(true);
        chart3.setHorizontalScrollBarEnabled(true);
        //chart.setVisibleXRange(1,10);
        chart3.setVisibleXRangeMaximum(3);
        //chart.moveViewToX(5);
        chart3.moveViewTo(10,10, YAxis.AxisDependency.RIGHT);

        //chart.moveViewTo(0f,data.getDataSets(dataSet1), YAxis.AxisDependency.LEFT)
        //chart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
        //
        //chart.moveViewToX(data.getXMax());
    }


    public void addDataToGraphFlowB() {
        //databaseHelper = new DatabaseHelper(this);

        final ArrayList<Entry> yVals = new ArrayList<Entry>();
        final ArrayList<String> yData = databaseHelper.queryYDataFlowB();
        if (yData != null) {

            for (int j = 0; j < yData.size(); j++) {
                if(!TextUtils.isEmpty(yData.get(j))) {
                    Entry lineEntry = new Entry(j, Float.parseFloat(yData.get(j)));
                    yVals.add(lineEntry);
                }
            }
        }
        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = databaseHelper.queryXData();

        for (int i = 0; i < databaseHelper.queryXData().size(); i++) {
            xVals.add(xData.get(i));

        }


        // Utils.init(getApplicationContext());

        //data.addDataSet(new Entry(x, y)0);
        chart4.refreshDrawableState();
        dataSet = new LineDataSet(yVals, "Flow Rate B in Litre/min");
        dataSet1 = new ArrayList<>();
        dataSet1.add(dataSet);
        Utils.init(getApplicationContext());
        data = new LineData(dataSet1);
        chart4.clear();
        data.notifyDataChanged();
        chart4.notifyDataSetChanged();
        chart4.setData(data);
        chart4.invalidate();
        chart4.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart4.getDescription().setEnabled(true);
        chart4.getDescription().setText("X axis = TIME , Y axis = Litre/min");
        chart4.setTouchEnabled(true);
        chart4.setDragEnabled(true);
        chart4.setScaleEnabled(true);
        chart4.setPinchZoom(true);
        chart4.setHorizontalScrollBarEnabled(true);
        //chart.setVisibleXRange(1,10);
        chart4.setVisibleXRangeMaximum(3);
        //chart.moveViewToX(5);
        chart4.moveViewTo(10,10, YAxis.AxisDependency.RIGHT);

        //chart.moveViewTo(0f,data.getDataSets(dataSet1), YAxis.AxisDependency.LEFT)
        //chart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
        //
        //chart.moveViewToX(data.getXMax());
    }


}